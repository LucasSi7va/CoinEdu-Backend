package com.ProjetoExtensao.CoinEdu.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.ProjetoExtensao.CoinEdu.dto.MoedaDto;
import com.ProjetoExtensao.CoinEdu.model.Moeda;
import jdk.dynalink.linker.LinkerServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class ElasticService {

private final ElasticsearchClient elasticsearchClient;
private final ServiceMoedaAPI serviceMoedaAPI;


public void sincronizarMoedas() throws IOException {
    List<MoedaDto> moedas = serviceMoedaAPI.getMercado();

    BulkRequest.Builder bulk = new BulkRequest.Builder();

    moedas.forEach(moeda -> {
        Moeda m = new Moeda();
        m.setId(moeda.id());
        m.setNome(moeda.nome());
        m.setSimbolo(moeda.symbol());
        m.setPrecoAtual(BigDecimal.valueOf(moeda.precoAtual()));
        m.setMarketCap(BigDecimal.valueOf(moeda.marketCap()));


        bulk.operations(op -> op.index(i -> i.index("moedas").id(moeda.id()).document(m)));

    });
    elasticsearchClient.bulk(bulk.build());
}


public List<Moeda> buscarPorNome(String nome) throws IOException {
    SearchResponse<Moeda> response = elasticsearchClient.search(s -> s
            .index("moedas")
            .query(q -> q
                    .match(m -> m
                            .field("nome")
                            .query(nome))) ,
    Moeda.class);

return response.hits().hits().stream().map(Hit::source).toList();
}


public List<Moeda> listarTudo() throws IOException {
        SearchResponse<Moeda> response = elasticsearchClient.search(s -> s
                        .index("moedas")
                        .query(q -> q.matchAll(m -> m)),
                Moeda.class);

        return response.hits().hits()
                .stream()
                .map(Hit::source)
                .toList();
    }

    public List<Moeda> buscarFuzzy(String nome) throws IOException {
        SearchResponse<Moeda> response = elasticsearchClient.search(s -> s
                        .index("moedas")
                        .query(q -> q
                                .fuzzy(f -> f
                                        .field("nome")
                                        .value(nome)
                                        .fuzziness("AUTO"))),
                Moeda.class);

        return response.hits().hits()
                .stream()
                .map(Hit::source)
                .toList();
    }
}
