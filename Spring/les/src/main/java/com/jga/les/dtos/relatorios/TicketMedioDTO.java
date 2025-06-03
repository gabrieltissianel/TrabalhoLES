package com.jga.les.dtos.relatorios;

import com.jga.les.model.Cliente;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketMedioDTO {
    private String cliente;

    @PdfFormat(numberPattern = "R$ #,##0.00")
    private Double ticket_medio;

    // Construtor, Getters e Setters
    public TicketMedioDTO(Cliente cliente, Double valorMedio) {
        this.cliente = cliente.getNome();
        this.ticket_medio = valorMedio;
    }
}