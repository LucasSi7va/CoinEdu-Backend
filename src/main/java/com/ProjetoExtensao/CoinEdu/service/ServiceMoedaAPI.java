package com.ProjetoExtensao.CoinEdu.service;

import com.ProjetoExtensao.CoinEdu.dto.HistoricoDto;
import com.ProjetoExtensao.CoinEdu.dto.MoedaDto;
import com.ProjetoExtensao.CoinEdu.dto.filtroGlobal.FiltroGlobal;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public MoedaDto getMoedaPorId(String moedaId) {
        return getMercado()
                .stream()
                .filter(m -> m.id().equalsIgnoreCase(moedaId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Moeda não encontrada: " + moedaId));
    }


    public Map<String, Object> getHistorico(String moedaId, String data) {
        HistoricoDto historico = api.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/coins/{id}/history")
                        .queryParam("date", data)
                        .build(moedaId))
                .retrieve()
                .bodyToMono(HistoricoDto.class)
                .block();

        return Map.of(
                "moeda", historico.name(),
                "data", data,
                "preco_brl", historico.marketData().currentPrice().brl()
        );
    }

    public Map<String, Object> getHistoricoPorDias(String moedaId, int diasAtras) {
        String data = LocalDate.now()
                .minusDays(diasAtras)
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        return getHistorico(moedaId, data);
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

