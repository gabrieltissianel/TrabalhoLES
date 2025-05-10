package com.jga.les.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private String nome;

    private Double limite;

    private Double saldo;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date ultimo_dia_negativado;
    
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date dt_nascimento;
}
