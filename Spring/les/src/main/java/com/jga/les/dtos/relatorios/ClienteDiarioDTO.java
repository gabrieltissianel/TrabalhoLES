package com.jga.les.dtos.relatorios;

public class ClienteDiarioDTO {
    private String nome;
    private Double valorTotal;
    private String horaVenda;

    public ClienteDiarioDTO(String nome, Double valorTotal, String horaVenda) {
        this.nome = nome;
        this.valorTotal = valorTotal;
        this.horaVenda = horaVenda;
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public String getHoraVenda() {
        return horaVenda;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setHoraVenda(String horaVenda) {
        this.horaVenda = horaVenda;
    }

    // Setters (opcional, dependendo do uso)
    public void setNome(String nome) {
        this.nome = nome;
    }

    // ... outros setters
}