package com.jga.les.dtos.relatorios;

import java.sql.Date;

public record dreDTO(Date data, Double receber, Double pagar, Double resultado, Double saldo) {

}
