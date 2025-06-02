package com.jga.les.dtos.relatorios;

import java.util.Date;

public class DesempenhoDTO {
    private Date data;
    private double entrada;
    private double saida;
    private long clientes;

    public DesempenhoDTO(Date data, double entrada, double saida, long clientes) {
        this.data = data;
        this.entrada = entrada;
        this.saida = saida;
        this.clientes = clientes;
    }

    // Getters e Setters

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public double getEntrada() {
        return entrada;
    }

    public void setEntrada(double entrada) {
        this.entrada = entrada;
    }

    public double getSaida() {
        return saida;
    }

    public void setSaida(double saida) {
        this.saida = saida;
    }

    public long getClientes() {
        return clientes;
    }

    public void setClientes(long clientes) {
        this.clientes = clientes;
    }
}
