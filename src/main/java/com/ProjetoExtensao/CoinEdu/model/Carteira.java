package com.ProjetoExtensao.CoinEdu.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class Carteira {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Usuario usuario;

    @ManyToMany
    private List<Moeda> moedasFavoritas;

    private BigDecimal saldoSimulado;
}