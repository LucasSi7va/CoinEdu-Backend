package com.ProjetoExtensao.CoinEdu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@AllArgsConstructor  @Getter @Setter @NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome",nullable = false)
    private String nome;


    @Column(name = "email", nullable = false , unique = true)
    private String email;


    @Column(name = "senha", nullable = false , length = 60)
    private String senha;


    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Carteira carteira;


    @Column(name = "modo_idoso" , nullable = false)
    private Boolean modoIdoso = false;

}

