package com.ProjetoExtensao.CoinEdu.Controller;

import com.ProjetoExtensao.CoinEdu.Service.ServiceMoedaAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/coin")
public class ControllerCoin {
    @Autowired
    private ServiceMoedaAPI serviceMoedaAPI;

    @GetMapping("/{id}")
    public Mono<Double> verPreco(@PathVariable String id) {
        return serviceMoedaAPI.getPreco(id);
    }
}
