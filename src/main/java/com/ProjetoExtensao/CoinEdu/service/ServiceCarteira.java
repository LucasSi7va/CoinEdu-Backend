package com.ProjetoExtensao.CoinEdu.service;

import com.ProjetoExtensao.CoinEdu.dto.MoedaDto;
import com.ProjetoExtensao.CoinEdu.dto.SimulacaoDto;
import com.ProjetoExtensao.CoinEdu.model.Carteira;
import com.ProjetoExtensao.CoinEdu.model.Usuario;
import com.ProjetoExtensao.CoinEdu.repository.CarteiraRepository;
import com.ProjetoExtensao.CoinEdu.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@AllArgsConstructor
public class ServiceCarteira {
    @Autowired
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private final CarteiraRepository carteiraRepository;
    @Autowired
    private final ServiceMoedaAPI serviceMoedaAPI;


    public ResponseEntity<String> salvarCarteira(Long idUsuario, String moeda) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        Carteira carteira = usuario.getCarteira();

        boolean existe = serviceMoedaAPI.getMercado()
                .stream()
                .anyMatch(moedaDto -> moedaDto.id().equals(moeda));

        if (!existe) {
            throw new RuntimeException("Moeda nao encontrada");
        }

        if (carteira.getMoedasFavoritas().contains(moeda)) {
            throw new RuntimeException("Moeda ja esta na carteira");
        }

        carteira.getMoedasFavoritas().add(moeda);

        carteiraRepository.save(carteira);

        return ResponseEntity.ok("Moeda adicionada com sucesso");
    }

    public ResponseEntity<String> removerCarteira(Long idUsuario, String moeda) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        Carteira carteira = usuario.getCarteira();

        boolean existe = serviceMoedaAPI.getMercado().stream().anyMatch(moedaDto -> moedaDto.id().equals(moeda));
        if (!existe) {
            throw new RuntimeException("Moeda nao encontrada");
        }

        if (!carteira.getMoedasFavoritas().contains(moeda)) {
            throw new RuntimeException("Moeda nao esta na carteira");
        }

        carteira.getMoedasFavoritas().remove(moeda);

        carteiraRepository.save(carteira);

        return ResponseEntity.ok("Moeda removida com sucesso");
    }



    public ResponseEntity<SimulacaoDto> simularCompra(Long usuarioId, String moedaId, BigDecimal valorCompra) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        boolean moedaCarteira = usuario.getCarteira().getMoedasFavoritas().contains(moedaId);

        if (!moedaCarteira) {
            throw new RuntimeException("Moeda nao esta na carteira");
        }

        MoedaDto moeda = serviceMoedaAPI.getMoedaPorId(moedaId);

        BigDecimal precoAtual = BigDecimal.valueOf(moeda.precoAtual());
        BigDecimal quantidade = valorCompra.divide(precoAtual, 8, RoundingMode.HALF_UP);

        return ResponseEntity.ok(new SimulacaoDto(
                moeda.id(),
                moeda.nome(),
                valorCompra,
                precoAtual,
                quantidade
        ));
    }

}
