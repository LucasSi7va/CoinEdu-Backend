package com.ProjetoExtensao.CoinEdu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "moeda")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Moeda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , unique = true)
    private String sigla;
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false , precision = 20, scale = 10)
    private BigDecimal precoAtual;

    @Column(precision = 25, scale = 2)
    private BigDecimal marketCap;

    private LocalDateTime ultimaAtualizacao;

}
