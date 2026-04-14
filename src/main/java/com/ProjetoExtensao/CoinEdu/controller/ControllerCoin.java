package com.ProjetoExtensao.CoinEdu.controller;

import com.ProjetoExtensao.CoinEdu.dto.MoedaDto;
import com.ProjetoExtensao.CoinEdu.dto.filtroGlobal.FiltroGlobal;
import com.ProjetoExtensao.CoinEdu.service.ServiceMoedaAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/coin")
public class ControllerCoin {
    @Autowired
    private ServiceMoedaAPI serviceMoedaAPI;

    @GetMapping
    public Mono<List<MoedaDto>> verPreco() {
        return serviceMoedaAPI.getMercado();
    }

    @GetMapping("/nome/{nome}")
    public Mono<ResponseEntity<List<MoedaDto>>> buscarNome(@RequestParam String nome) {
        return serviceMoedaAPI.buscarNome(nome)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/filtra")
    public Mono<ResponseEntity<List<MoedaDto>>> buscarFiltro(FiltroGlobal filtroGlobal) {
        return serviceMoedaAPI.buscarFiltro(filtroGlobal)
                .map(ResponseEntity::ok);
    }


}
