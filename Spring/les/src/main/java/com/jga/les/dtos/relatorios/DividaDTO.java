package com.jga.les.dtos.relatorios;

import com.jga.les.model.Cliente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZoneId;

@Getter
@Setter
public class DividaDTO {
    private String nome;
    @PdfFormat(numberPattern = "R$ #,##0.00")
    private double saldo_negativo;
    @PdfFormat(numberPattern = "R$ #,##0.00")
    private double limite;

    @PdfFormat()
    private LocalDate data_negativado;

    public DividaDTO(Cliente cliente) {
        this.nome = cliente.getNome();
        this.saldo_negativo = cliente.getSaldo();
        this.limite = cliente.getLimite();
        this.data_negativado = cliente.getUltimo_dia_negativado().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}