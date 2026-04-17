package com.ProjetoExtensao.CoinEdu.service;

import com.ProjetoExtensao.CoinEdu.dto.MoedaDto;
import com.ProjetoExtensao.CoinEdu.dto.filtroGlobal.FiltroGlobal;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;


@Service
public class ServiceMoedaAPI {

    private final WebClient api = WebClient.create("https://api.coingecko.com/api/v3");

    @Cacheable("moeda")
    @CircuitBreaker(name = "coinGecko", fallbackMethod = "fallbackSimlpes")
    public List<MoedaDto> getMercado() {
        return api.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/coins/markets")
                        .queryParam("vs_currency", "brl")
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MoedaDto>>() {})
                .block();
    }

    public List<MoedaDto> fallbackSimlpes(Throwable t) {
        System.out.println("Erro na CoinGecko: " + t.getMessage());
        return List.of();
    }

    public List<MoedaDto> buscarNome(String nome) {
        return getMercado().stream()
                .filter(moedaDto -> moedaDto.nome()
                        .toLowerCase()
                        .contains(nome.toLowerCase()))
                .toList();
    }


    public List<MoedaDto> buscarFiltro(FiltroGlobal filtroGlobal) {
        return getMercado().stream()
                .filter(m -> (filtroGlobal.precoMax() == null || m.precoAtual() <= filtroGlobal.precoMax()))
                .filter(m -> (filtroGlobal.rank() == null || m.rank() >= filtroGlobal.rank()))
                .toList();
    }
}

