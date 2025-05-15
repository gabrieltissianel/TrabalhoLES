package com.jga.les.dtos;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConsumoDiarioClienteDto {
    private Long id;
    private String nome;
    private Date entrada;
    private Double preco;
    private Double custo;
}
