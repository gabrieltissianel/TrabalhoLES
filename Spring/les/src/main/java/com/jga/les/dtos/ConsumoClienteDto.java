package com.jga.les.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConsumoClienteDto {
    private Long id;
    private String nome;
    private Double valor_total;
}
