package com.ProjetoExtensao.CoinEdu.controller;

import com.ProjetoExtensao.CoinEdu.dto.MoedaDto;
import com.ProjetoExtensao.CoinEdu.dto.filtroGlobal.FiltroGlobal;
import com.ProjetoExtensao.CoinEdu.service.ServiceMoedaAPI;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/coin")
@AllArgsConstructor
public class ControllerCoin {
    @Autowired
    private final ServiceMoedaAPI serviceMoedaAPI;


    @GetMapping
    public List<MoedaDto> verPreco() {
        return serviceMoedaAPI.getMercado();
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<MoedaDto>> buscarNome(@RequestParam String nome) {
        return ResponseEntity.ok(serviceMoedaAPI.buscarNome(nome));
    }

    @GetMapping("/filtra")
    public ResponseEntity<List<MoedaDto>> buscarFiltro(FiltroGlobal filtroGlobal) {
        return ResponseEntity.ok(serviceMoedaAPI.buscarFiltro(filtroGlobal));
    }

}
