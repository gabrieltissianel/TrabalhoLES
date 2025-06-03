package com.jga.les.dtos.relatorios;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jga.les.model.Cliente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class AniversarianteDTO {

    private String nome;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate nascimento;
    private int idade;

    // Construtor a partir da entidade Cliente (com cálculo da idade)
    public AniversarianteDTO(Cliente cliente) {
        this.nome = cliente.getNome();
        this.nascimento = cliente.getDt_nascimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.idade = calcularIdade(cliente.getDt_nascimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    // Método para calcular idade (similar ao ClienteDTO)
    private int calcularIdade(LocalDate dataNasc) {
        return LocalDate.now().getYear() - dataNasc.getYear();
    }

}