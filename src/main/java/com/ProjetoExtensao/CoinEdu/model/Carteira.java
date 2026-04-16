package com.ProjetoExtensao.CoinEdu.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "carteira")
public class Carteira {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "usuario_id")
    @OneToOne
    private Usuario usuario;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "moedas_favoritas" , joinColumns = @JoinColumn(name = "carteira_id"))
    @Column(name = "moeda_id")
    private List<String> moedasFavoritas;

    @Column(precision = 25, scale = 2)
    private BigDecimal saldoSimulado;
}