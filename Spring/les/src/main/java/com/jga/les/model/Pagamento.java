package com.jga.les.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double valor;
    
    @DateTimeFormat
    private Date dt_vencimento;

    @DateTimeFormat
    private Date dt_pagamento;

    @ManyToOne
    private Fornecedor fornecedor;
}