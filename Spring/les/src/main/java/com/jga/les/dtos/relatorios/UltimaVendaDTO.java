package com.jga.les.dtos.relatorios;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class UltimaVendaDTO {
    //private final Long vendaId;
    //private final Long clienteId;
    private final String nome;

    @PdfFormat(datePattern = "dd/MM/yyyy HH:mm")
    private final LocalDateTime data_da_venda;

    @PdfFormat(numberPattern = "R$ #,##0.00")
    private BigDecimal valor;

    // Construtor principal
    public UltimaVendaDTO(Long vendaId, Long clienteId, String clienteNome,
                          LocalDateTime dataVenda, Double valor) {
        //this.vendaId = Objects.requireNonNull(vendaId, "ID da venda não pode ser nulo");
        //this.clienteId = Objects.requireNonNull(clienteId, "ID do cliente não pode ser nulo");
        this.nome = Objects.requireNonNull(clienteNome, "Nome do cliente não pode ser nulo");
        this.data_da_venda = dataVenda;
        this.valor = BigDecimal.valueOf(valor);
    }

    // Construtor para consultas nativas - versão mais robusta
    public UltimaVendaDTO(Object[] resultado) {
        if (resultado == null || resultado.length < 5) {
            throw new IllegalArgumentException("Resultado inválido: array deve ter 5 elementos");
        }

        try {
            //this.vendaId = (Long) resultado[0];
            //this.clienteId = (Long) resultado[1];
            this.nome = convertToString(resultado[2], "Nome do cliente");
            this.data_da_venda = convertToLocalDateTime(resultado[3]);
            this.valor = BigDecimal.valueOf(convertToDouble(resultado[4]));
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao processar resultado da consulta", e);
        }
    }

    // Métodos auxiliares de conversão
    private UUID convertToUUID(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " não pode ser nulo");
        }
        if (value instanceof UUID) {
            return (UUID) value;
        }
        try {
            return UUID.fromString(value.toString());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(fieldName + " em formato inválido", e);
        }
    }

    private String convertToString(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " não pode ser nulo");
        }
        return value.toString();
    }

    private LocalDateTime convertToLocalDateTime(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Timestamp) {
            return ((Timestamp) value).toLocalDateTime();
        }
        if (value instanceof java.sql.Date) {
            return ((java.sql.Date) value).toLocalDate().atStartOfDay();
        }
        if (value instanceof Date) {
            return ((Date) value).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        }
        if (value instanceof LocalDateTime) {
            return (LocalDateTime) value;
        }
        if (value instanceof LocalDate) {
            return ((LocalDate) value).atStartOfDay();
        }
        throw new IllegalArgumentException("Tipo de data não suportado: " + value.getClass());
    }

    private Double convertToDouble(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor monetário em formato inválido", e);
        }
    }

    // Getters
//    public Long getVendaId() { return vendaId; }
//    public Long getClienteId() { return clienteId; }
    public String getClienteNome() { return nome; }
    public LocalDateTime getDataVenda() { return data_da_venda; }
    public BigDecimal getValor() { return valor; }
}