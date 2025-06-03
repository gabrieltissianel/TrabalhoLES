package com.jga.les.controller;

import com.jga.les.dtos.relatorios.*;
import com.jga.les.model.Cliente;
import com.jga.les.pdfGenerator.GenericPDF;
import com.jga.les.pdfGenerator.PlotUtils;
import com.jga.les.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/relatorios")
@Validated
@Tag(name = "Relatórios", description = "Endpoints para geração de relatórios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    @Autowired
    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    // Ticket médio - PDF e JSON
    @Operation(summary = "Ticket médio por período (PDF)")
    @GetMapping(value = "/pdf/ticketMedio", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getTicketMedioMultiplosClientesPDF(
            @Parameter(description = "Data inicial (yyyy-MM-dd)", example = "2023-05-01")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataInicio,
            @Parameter(description = "Data final (yyyy-MM-dd)", example = "2023-05-31")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataFim) {

        List<TicketMedioDTO> resultados = relatorioService.getTicketMedioMultiplosClientes(dataInicio, dataFim);
        return ResponseEntity.ok(GenericPDF.gerarRelatorioBytes(resultados, "Ticket Médio"));
    }

    //FUNCIONANDO
    @Operation(summary = "Ticket médio por período (JSON)")
    @GetMapping(value = "/ticketMedio", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TicketMedioDTO>> getTicketMedioMultiplosClientesJSON(
            @Parameter(description = "Data inicial (yyyy-MM-dd)", example = "2023-05-01")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataInicio,
            @Parameter(description = "Data final (yyyy-MM-dd)", example = "2023-05-31")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataFim) {

        List<TicketMedioDTO> resultados = relatorioService.getTicketMedioMultiplosClientes(dataInicio, dataFim);
        return ResponseEntity.ok(resultados);
    }

    // Última venda por cliente - PDF e JSON
    @Operation(summary = "Última venda por cliente (PDF)")
    @GetMapping(value = "/pdf/ultimasVendas/{clienteId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getUltimaVendaPDF(
            @Parameter(description = "ID do cliente")
            @PathVariable Long clienteId) {

        UltimaVendaDTO resultado = relatorioService.getUltimaVenda(clienteId);
        return ResponseEntity.ok(GenericPDF.gerarRelatorioBytes(List.of(resultado), "Última Venda"));
    }


    @Operation(summary = "Última venda por cliente (JSON)")
    @GetMapping(value = "/ultimasVendas/{clienteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UltimaVendaDTO> getUltimaVendaJSON(
            @Parameter(description = "ID do cliente")
            @PathVariable Long clienteId) {

        UltimaVendaDTO resultado = relatorioService.getUltimaVenda(clienteId);
        return ResponseEntity.ok(resultado);
    }

    // Últimas vendas de todos clientes - PDF e JSON
    @Operation(summary = "Última venda de cada cliente (PDF)",
            description = "Retorna a última venda registrada para cada cliente em PDF")
    @GetMapping(value = "/pdf/ultimasVendas", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getUltimasVendasTodosClientesPDF() {
        List<UltimaVendaDTO> resultados = relatorioService.getUltimasVendasTodosClientes();
        return ResponseEntity.ok(GenericPDF.gerarRelatorioBytes(resultados, "Últimas Vendas por Cliente"));
    }

    // FUNCIONANDO GABRIEL
    @Operation(summary = "Última venda de cada cliente (JSON)",
            description = "Retorna a última venda registrada para cada cliente em JSON")
    @GetMapping(value = "/ultimasVendas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UltimaVendaDTO>> getUltimasVendasTodosClientesJSON() {
        List<UltimaVendaDTO> resultados = relatorioService.getUltimasVendasTodosClientes();
        return ResponseEntity.ok(resultados);
    }

    // Vendas diárias - PDF e JSON
    @Operation(summary = "Vendas por data específica (PDF)")
    @GetMapping(value = "/pdf/diario/{data}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getVendasDiariasPDF(
            @Parameter(description = "Data no formato yyyy-MM-dd", example = "2023-05-15")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date data) {

        List<ClienteDiarioDTO> resultados = relatorioService.getVendasPorData(data);
        return ResponseEntity.ok(GenericPDF.gerarRelatorioBytes(resultados, "Vendas Diárias"));
    }

    //FUNCIONANDO GABRIEL
    @Operation(summary = "Vendas por data específica (JSON)")
    @GetMapping(value = "/diario/{data}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClienteDiarioDTO>> getVendasDiariasJSON(
            @Parameter(description = "Data no formato yyyy-MM-dd", example = "2023-05-15")
            @DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable Date data) {

        List<ClienteDiarioDTO> resultados = relatorioService.getVendasPorData(data);
        return ResponseEntity.ok(resultados);
    }

    // Vendas do dia atual - PDF e JSON
    @Operation(summary = "Vendas do dia atual (PDF)")
    @GetMapping(value = "/pdf/diario", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getVendasDoDiaAtualPDF() {
        List<ClienteDiarioDTO> resultados = relatorioService.getVendasDoDia();
        return ResponseEntity.ok(GenericPDF.gerarRelatorioBytes(resultados, "Vendas Hoje"));
    }

    //FUNCIONANDO GABRIEL
    @Operation(summary = "Vendas do dia atual (JSON)")
    @GetMapping(value = "/diario", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClienteDiarioDTO>> getVendasDoDiaAtualJSON() {
        List<ClienteDiarioDTO> resultados = relatorioService.getVendasDoDia();
        return ResponseEntity.ok(resultados);
    }

//    // Aniversariantes do dia - PDF e JSON
//    @Operation(summary = "Aniversariantes do dia (PDF)")
//    @GetMapping(value = "/pdf/aniversariantesdia", produces = MediaType.APPLICATION_PDF_VALUE)
//    public ResponseEntity<byte[]> getAniversariantesDoDiaPDF() {
//        List<AniversarianteDTO> resultados = relatorioService.getAniversariantesDoDia();
//        return ResponseEntity.ok(GenericPDF.gerarRelatorioBytes(resultados, "Aniversariantes do Dia"));
//    }
//
//    @Operation(summary = "Aniversariantes do dia (JSON)")
//    @GetMapping(value = "/aniversariantesdia", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<AniversarianteDTO>> getAniversariantesDoDiaJSON() {
//        List<AniversarianteDTO> resultados = relatorioService.getAniversariantesDoDia();
//        return ResponseEntity.ok(resultados);
//    }

    // Aniversariantes do mês - PDF e JSON
    @Operation(summary = "Aniversariantes do mês (PDF)")
    @GetMapping(value = "/pdf/aniversariantes", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getAniversariantesDoMesPDF(
            @Parameter(description = "Mês (1-12)", example = "5")
            @RequestParam int mes) {

        List<AniversarianteDTO> resultados = relatorioService.getAniversariantesDoMes(mes);
        return ResponseEntity.ok(GenericPDF.gerarRelatorioBytes(resultados, "Aniversariantes do Mês"));
    }

    // FUNCIONANDO GABRIEL
    @Operation(summary = "Aniversariantes do mês (JSON)")
    @GetMapping(value = "/aniversariantes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AniversarianteDTO>> getAniversariantesDoMesJSON(
            @Parameter(description = "Mês (1-12)", example = "5")
            @RequestParam int mes) {

        List<AniversarianteDTO> resultados = relatorioService.getAniversariantesDoMes(mes);
        return ResponseEntity.ok(resultados);
    }

    // Clientes endividados - PDF e JSON
    @Operation(summary = "Clientes endividados (PDF)")
    @GetMapping(value = "/pdf/clientesEmAberto", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getClientesEndividadosPDF() {
        List<Cliente> resultados = relatorioService.getClientesEndividados();
        return ResponseEntity.ok(GenericPDF.gerarRelatorioBytes(resultados, "Clientes Endividados"));
    }

    // FUNCIONANDO GABRIEL, ACERTAR FORMATAÇAO DATA NO CLIENTE
    @Operation(summary = "Clientes endividados (JSON)")
    @GetMapping(value = "/clientesEmAberto", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Cliente>> getClientesEndividadosJSON() {
        List<Cliente> resultados = relatorioService.getClientesEndividados();
        return ResponseEntity.ok(resultados);
    }

    // Produtos Serial Vendidos por Período - PDF e JSON
    @Operation(summary = "Produtos Serial Vendidos por Período (PDF)")
    @GetMapping(value = "/pdf/produtosSerial", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getProdutosSerialVendidosPDF(
            @Parameter(description = "Data inicial (yyyy-MM-dd)", example = "2023-01-01", required = true)
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataInicio,

            @Parameter(description = "Data final (yyyy-MM-dd)", example = "2023-12-31", required = true)
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataFim) {

        List<ProdutoRelatorioDTO> resultados = relatorioService.getRelatorioProdutos(dataInicio, dataFim);
        return ResponseEntity.ok(GenericPDF.gerarRelatorioBytes(resultados, "Produtos Serial Vendidos"));
    }

    //FUNCIONANDO GABRIEL
    @Operation(summary = "Produtos Serial Vendidos por Período (JSON)")
    @GetMapping(value = "/produtosSerial", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProdutoRelatorioDTO>> getProdutosSerialVendidosJSON(
            @Parameter(description = "Data inicial (yyyy-MM-dd)", example = "2023-01-01", required = true)
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataInicio,

            @Parameter(description = "Data final (yyyy-MM-dd)", example = "2023-12-31", required = true)
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataFim) {

        List<ProdutoRelatorioDTO> resultados = relatorioService.getRelatorioProdutos(dataInicio, dataFim);
        return ResponseEntity.ok(resultados);
    }

    @GetMapping(value = "/pdf/consumoDiario", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getClienteDiarioPdf(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataInicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataFim) {

        // Gera o gráfico como imagem
        BufferedImage imagemGerada = PlotUtils.generateConsumoDiarioChart(
                relatorioService.getConsumoDiarioParaGrafico(dataInicio, dataFim),
                "Relatório Consumo Diário");

        // Converte BufferedImage em byte[]
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(imagemGerada, "png", byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

        byte[] imagemBytes = byteArrayOutputStream.toByteArray();

        // Gera o PDF com a imagem
        byte[] pdf = GenericPDF.gerarRelatorioImagem(imagemBytes, "Relatório Cliente Diário");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping(value = "/consumoDiario", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getClienteDiario(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataInicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataFim) {

        try {
            // Gera o gráfico como imagem
            BufferedImage imagemGerada = PlotUtils.generateConsumoDiarioChart(
                    relatorioService.getConsumoDiarioParaGrafico(dataInicio, dataFim),
                    "Relatório Consumo Diário");

            // Converte a imagem em um array de bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imagemGerada, "png", baos);
            byte[] imagemBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imagemBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

//    @GetMapping("/pdf/dre-diario")
//    public ResponseEntity<byte[]> getDREPorDiaPdf(
//            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataInicio,
//            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataFim) {
//        List<DesempenhoDTO> resultados = relatorioService.gerarRelatorioDRE(dataInicio, dataFim);
//        return ResponseEntity.ok(GenericPDF.gerarRelatorioBytes(resultados, "Produtos Serial Vendidos"));
//    }
//
//    @GetMapping("/dre-diario")
//    public ResponseEntity<List<DesempenhoDTO>> getDREPorDia(
//            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataInicio,
//            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataFim) {
//        return ResponseEntity.ok(relatorioService.gerarRelatorioDRE(dataInicio, dataFim));
//    }

}