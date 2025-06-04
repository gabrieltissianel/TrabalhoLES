package com.jga.les.dtos.relatorios;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConsumoGraficoDTO {
    private String cliente;
    private Double consumo;
}