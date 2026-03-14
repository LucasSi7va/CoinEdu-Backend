package com.ProjetoExtensao.CoinEdu.Service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;


@Service
public class ServiceMoedaAPI {

    private final WebClient api = WebClient.create("https://api.coingecko.com/api/v3");

    //@CircuitBreaker(name = "coinGecko" , fallbackMethod = "fallbackSimlpes")
    public Mono<Double> getPreco(String id) {
        return api.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/simple/price")
                        .queryParam("ids", id)
                        .queryParam("vs_currencies", "brl")
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(map -> Mono.justOrEmpty((Map<String, Object>) map.get(id)))
                .map(moeda -> ((Number) moeda.get("brl")).doubleValue());
    }


//
//    public Mono<Double> fallbackSimlpes(String id , Throwable t) {
//        System.out.println("Erro na CoinGecko: " + t.getMessage());
//        return Mono.just(0.0);
//    }


}
