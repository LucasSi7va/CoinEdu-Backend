package com.ProjetoExtensao.CoinEdu.service;

import com.ProjetoExtensao.CoinEdu.dto.*;
import com.ProjetoExtensao.CoinEdu.dto.filtroGlobal.FiltroGlobal;
import com.ProjetoExtensao.CoinEdu.model.Carteira;
import com.ProjetoExtensao.CoinEdu.model.Usuario;
import com.ProjetoExtensao.CoinEdu.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class ServiceUsuario {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ServiceMoedaAPI serviceMoedaAPI;
    @Autowired
    private EmailService emailService;


    private final Map<String, PendenteCadastroDto> pendentes = new ConcurrentHashMap<>();

    public final Map<String , Integer> tentativas = new ConcurrentHashMap<>();


    public ResponseEntity<UsuarioDto> getIdUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Id nao encontrado"));

        return ResponseEntity.ok(new UsuarioDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha())
        );
    }



    public ResponseEntity<CadastroResponseDto> cadastrarUsuario(Usuario usuario , String ip) {

        int count = tentativas.getOrDefault(ip , 0);

        if (count >= 3) {
            throw new RuntimeException("Muitas tentativas , tente novamente mais tarde. ");

        }
        tentativas.merge(ip , 1 , Integer::sum);

        CompletableFuture.delayedExecutor(10, TimeUnit.MINUTES)
                .execute(() -> tentativas.remove(ip));

        usuario.setNome(usuario.getNome().trim());

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Email ja cadastrado");
        }

        if (pendentes.containsKey(usuario.getEmail())) {
            throw new RuntimeException("Confirmação já enviada. Verifique seu email.");
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        String codigo = emailService.gerarCodigo();

        pendentes.put(usuario.getEmail() , new PendenteCadastroDto(
                usuario , codigo , LocalDateTime.now().plusMinutes(15)
        ));

        emailService.enviarCodigo(usuario.getEmail() , codigo , "cadastro");

        return ResponseEntity.ok( new CadastroResponseDto(
                    null  ,  "Codigo enviado para " + usuario.getEmail()
        ));
    }



    public ResponseEntity<UsuarioDto> confirmarCadastro(String email, String codigo) {
        PendenteCadastroDto pendente = pendentes.get(email);

        if (pendente == null) {
            throw new RuntimeException("Conta já verificada");
        }

        if (!pendente.codigo().equals(codigo)) {
            throw new RuntimeException("Código inválido");
        }

        if (LocalDateTime.now().isAfter(pendente.expiracao())) {
            pendentes.remove(email);
            throw new RuntimeException("Código expirado");
        }

        Usuario usuario = pendente.usuario();
        usuario.setVerificado(true);

        Carteira carteira = new Carteira();
        carteira.setUsuario(usuario);
        carteira.setMoedasFavoritas(new ArrayList<>());
        carteira.setSaldoSimulado(BigDecimal.ZERO);
        usuario.setCarteira(carteira);

        usuarioRepository.save(usuario);
        pendentes.remove(email);


        return ResponseEntity.ok( new UsuarioDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha()
        ));
    }



    public ResponseEntity<UsuarioCarteiraDTO> acessarLogin(FiltroGlobal filtroGlobal) {

        Usuario usuario = usuarioRepository.buscarPorCompleto(filtroGlobal.email() , filtroGlobal.nome()).orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));



       List<MoedaDto> favoritas = serviceMoedaAPI.getMercado()
               .stream()
               .filter(m -> usuario.getCarteira()
                       .getMoedasFavoritas()
                       .contains(m.id())).toList();

        return ResponseEntity.ok(new UsuarioCarteiraDTO(
        usuario.getNome() ,
        usuario.getEmail() ,
        favoritas
        ));

    }

    public ResponseEntity<ModoIdosoDto> modoIdoso(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        boolean valorAtual = usuario.getModoIdoso() != null && usuario.getModoIdoso();
        usuario.setModoIdoso(!valorAtual);

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(new ModoIdosoDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getModoIdoso()
        ));
    }


}
