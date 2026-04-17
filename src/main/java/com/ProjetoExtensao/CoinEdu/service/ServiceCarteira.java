package com.ProjetoExtensao.CoinEdu.service;

import com.ProjetoExtensao.CoinEdu.model.Carteira;
import com.ProjetoExtensao.CoinEdu.model.Usuario;
import com.ProjetoExtensao.CoinEdu.repository.CarteiraRepository;
import com.ProjetoExtensao.CoinEdu.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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


}
