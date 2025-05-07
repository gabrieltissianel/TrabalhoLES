package com.jga.les.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Data;

@Data
@Entity
public class CompraProduto {
    @EmbeddedId
    private CompraProdutoKey id;
    
    @ManyToOne
    @MapsId("idcompra")
    @JoinColumn(name = "compra_id")
    private Compra compra;

    @ManyToOne
    @MapsId("idproduto")
    @JoinColumn(name = "produto_id")
    private Produto produto;

    private Integer qntd;
    private Double preco;
    private Double custo;
}
