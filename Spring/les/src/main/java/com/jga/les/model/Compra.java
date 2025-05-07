package com.jga.les.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @CreationTimestamp
    private Date entrada;

    private Date saida;

    @ManyToOne
    private Cliente cliente;

    @OneToMany(mappedBy = "compra", orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    @JsonManagedReference
    private List<CompraProduto> compraProdutos;
}
