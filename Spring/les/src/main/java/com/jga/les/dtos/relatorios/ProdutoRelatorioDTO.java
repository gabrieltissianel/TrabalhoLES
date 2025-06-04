package com.jga.les.dtos.relatorios;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para relatório de produtos vendidos em um período")
public class ProdutoRelatorioDTO {

    @Schema(description = "Código do produto", example = "PRD-001")
    private String codigo;

    @Schema(description = "Nome do produto", example = "Smartphone XYZ")
    private String nome;

    @Schema(description = "Valor unitário do produto", example = "999.99")
    @PdfFormat(numberPattern = "R$ #,##0.00")
    private Double valor;

    @Schema(description = "Quantidade total vendida no período", example = "15")
    @PdfFormat(numberPattern = "# unidades")
    private Integer quantidade;

    // Construtores
    public ProdutoRelatorioDTO() {
    }

    public ProdutoRelatorioDTO(String codigoProduto, String nomeProduto, Double valorUnitario, Integer quantidadeVendida) {
        this.codigo = codigoProduto;
        this.nome = nomeProduto;
        this.valor = valorUnitario;
        this.quantidade = quantidadeVendida;
    }

    // Getters e Setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}