package com.jga.les.model;

import java.util.List;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nome;

    private String codigo;

    private boolean unitario;

    @NotNull
    private Double preco;

    @NotNull
    private Double custo;

    @OneToMany(mappedBy = "produto", orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    @JsonBackReference
    private List<CompraProduto> compraProdutos;
}
