package com.ProjetoExtensao.CoinEdu.service;

import com.ProjetoExtensao.CoinEdu.dto.ModoIdosoDto;
import com.ProjetoExtensao.CoinEdu.dto.MoedaDto;
import com.ProjetoExtensao.CoinEdu.dto.UsuarioCarteiraDTO;
import com.ProjetoExtensao.CoinEdu.dto.UsuarioDto;
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
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ServiceUsuario {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ServiceMoedaAPI serviceMoedaAPI;


    public ResponseEntity<UsuarioDto> getIdUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Id nao encontrado"));

        return ResponseEntity.ok(new UsuarioDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha())
        );
    }


    public ResponseEntity<UsuarioDto> cadastrarUsuario(Usuario usuario) {

        usuario.setNome(usuario.getNome().trim());

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Email ja cadastrado");
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));


        Carteira carteira = new Carteira();
        carteira.setUsuario(usuario);
        carteira.setMoedasFavoritas(new ArrayList<>());
        carteira.setSaldoSimulado(BigDecimal.ZERO);

        usuario.setCarteira(carteira);

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(
                new UsuarioDto(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getSenha())
        );
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
