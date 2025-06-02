package com.jga.les.dtos.relatorios;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jga.les.model.Cliente;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public class AniversarianteDTO {
    private Long id;
    private String nome;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date nascimento;

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Construtor a partir da entidade Cliente (com cálculo da idade)
    public AniversarianteDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
    }

    // Método para calcular idade (similar ao ClienteDTO)
    private int calcularIdade(Date nascimento) {
        LocalDate dataNasc = nascimento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return LocalDate.now().getYear() - dataNasc.getYear();
    }

    // Getters (não incluo setters pois o DTO é imutável após construção)
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

}