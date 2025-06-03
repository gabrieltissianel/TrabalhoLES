package com.jga.les.dtos.relatorios;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jga.les.model.Cliente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZoneId;

@Getter
@Setter
public class DividaDTO {
    private String nome;
    private double saldo;
    private double limite;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data_negativado;

    public DividaDTO(Cliente cliente) {
        this.nome = cliente.getNome();
        this.saldo = cliente.getSaldo();
        this.limite = cliente.getLimite();
        this.data_negativado = cliente.getUltimo_dia_negativado().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}