package com.jga.les.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    @JsonBackReference
    private Compra compra;

    @ManyToOne
    @MapsId("idproduto")
    @JoinColumn(name = "produto_id")
    @JsonManagedReference  
    private Produto produto;

    private Double qntd;
    private Double preco;
    private Double custo;
}
