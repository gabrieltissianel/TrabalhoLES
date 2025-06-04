package com.jga.les.dtos.relatorios;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class dreDTO {

    private String dia;

    @PdfFormat(numberPattern = "R$ #,##0.00")
    private Double receber;
    @PdfFormat(numberPattern = "R$ #,##0.00")
    private Double pagar;
    @PdfFormat(numberPattern = "R$ #,##0.00")
    private Double resultado;
    @PdfFormat(numberPattern = "R$ #,##0.00")
    private Double saldo;

}
