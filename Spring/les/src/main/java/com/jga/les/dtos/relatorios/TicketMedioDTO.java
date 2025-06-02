package com.jga.les.dtos.relatorios;

import com.jga.les.model.Cliente;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketMedioDTO {
    private Cliente cliente;
    private Double valor;

    // Construtor, Getters e Setters
    public TicketMedioDTO(Cliente cliente, Double valorMedio) {
        this.cliente = cliente;
        this.valor = valorMedio;
    }
}