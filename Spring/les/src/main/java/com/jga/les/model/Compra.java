package com.jga.les.model;

import java.util.Date;
import java.util.List;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private Date entrada;

    private Date saida;

    @ManyToOne
    private Cliente cliente;

    @OneToMany(mappedBy = "compra")
    private List<CompraProduto> compraProdutos;
}
