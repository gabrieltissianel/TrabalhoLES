package com.jga.les.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Usuario{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String nome;

    @NotNull
    @Column(unique = true)
    private String login;

    @NotNull
    private String senha;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Permissao> permissoes;
}