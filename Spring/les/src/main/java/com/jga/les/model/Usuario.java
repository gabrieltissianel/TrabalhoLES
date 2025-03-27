package com.jga.les.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Usuario {
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

    @OneToMany(mappedBy = "usuario")
    private List<Permissao> permissoes;

    // @Override
    public String getPassword() {
        return this.senha;
    }

    // @Override
    public String getUsername() {
        return this.login;
    }
}
