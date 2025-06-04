package com.jga.les.dtos.relatorios;

public class ClienteDiarioDTO {
    private String nome;

    @PdfFormat(numberPattern = "R$ #,##0.00")
    private Double valor;

    private String horario;

    public ClienteDiarioDTO(String nome, Double valorTotal, String horaVenda) {
        this.nome = nome;
        this.valor = valorTotal;
        this.horario = horaVenda;
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public Double getValor() {
        return valor;
    }

    public String getHorario() {
        return horario;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    // Setters (opcional, dependendo do uso)
    public void setNome(String nome) {
        this.nome = nome;
    }

    // ... outros setters
}