package com.jga.les.dtos.relatorios;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para relatório de produtos vendidos em um período")
public class ProdutoRelatorioDTO {

    @Schema(description = "Código do produto", example = "PRD-001")
    private String codigoProduto;

    @Schema(description = "Nome do produto", example = "Smartphone XYZ")
    private String nomeProduto;

    @Schema(description = "Valor unitário do produto", example = "999.99")
    private Double valorUnitario;

    @Schema(description = "Quantidade total vendida no período", example = "15")
    private Integer quantidadeVendida;

    // Construtores
    public ProdutoRelatorioDTO() {
    }

    public ProdutoRelatorioDTO(String codigoProduto, String nomeProduto, Double valorUnitario, Integer quantidadeVendida) {
        this.codigoProduto = codigoProduto;
        this.nomeProduto = nomeProduto;
        this.valorUnitario = valorUnitario;
        this.quantidadeVendida = quantidadeVendida;
    }

    // Getters e Setters
    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public Double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public Integer getQuantidadeVendida() {
        return quantidadeVendida;
    }

    public void setQuantidadeVendida(Integer quantidadeVendida) {
        this.quantidadeVendida = quantidadeVendida;
    }
}