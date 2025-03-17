package com.jga.les.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CompraProdutoKey implements Serializable {
    @Column(name = "compra_id")
    private long idcompra;

    @Column(name = "produto_id")
    private long idproduto;
}
