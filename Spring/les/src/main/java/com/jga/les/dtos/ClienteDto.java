package com.jga.les.dtos;

import java.sql.Date;

public record ClienteDto(Long id, String nome, Double limite, Double saldo, Date ultimo_dia_negativado, Date dt_nascimenDate) {

}
