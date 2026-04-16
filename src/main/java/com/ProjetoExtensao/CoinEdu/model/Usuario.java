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


    @JoinColumn(name = "carteira_id")
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true /* se o usuario for excluido a carteira tambem sera */  )
    private Carteira carteira;

}