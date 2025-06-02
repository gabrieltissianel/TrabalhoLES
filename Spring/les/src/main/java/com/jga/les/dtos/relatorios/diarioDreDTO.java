package com.jga.les.dtos.relatorios;

import java.util.Date;

public class diarioDreDTO {
    private Date dia;
    private float valorEntrada;
    private float valorSaida;
    private int quantidadeCliente;


    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public float getValorEntrada() {
        return valorEntrada;
    }

    public void setValorEntrada(float valorEntrada) {
        this.valorEntrada = valorEntrada;
    }

    public float getValorSaida() {
        return valorSaida;
    }

    public void setValorSaida(float valorSaida) {
        this.valorSaida = valorSaida;
    }

    public int getQuantidadeCliente() {
        return quantidadeCliente;
    }

    public void setQuantidadeCliente(int quantidadeCliente) {
        this.quantidadeCliente = quantidadeCliente;
    }
}
