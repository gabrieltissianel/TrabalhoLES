package com.jga.les.controller;

import java.io.IOException;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jga.les.dtos.ConsumoClienteDto;
import com.jga.les.repository.ClienteRepository;
import com.jga.les.repository.FornecedorRepository;
import com.jga.les.repository.RelatorioRepository;
import com.jga.les.service.JasperReportService;

import net.sf.jasperreports.engine.JRException;

@AllArgsConstructor
@RestController
@RequestMapping("/jasper")
public class JasperReportController {

    JasperReportService jasperReportService;

    FornecedorRepository fornecedorRepository;

    ClienteRepository clienteRepository;

    RelatorioRepository relatorioRepository;

    private ResponseEntity<Resource> sendFile(byte[] reportContent, String fileName) {
        ByteArrayResource resource = new ByteArrayResource(reportContent);
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .contentLength(resource.contentLength())
            .header(HttpHeaders.CONTENT_DISPOSITION.toString(),
                ContentDisposition.attachment()
                    .filename(fileName)
                    .build().toString())
            .body(resource);
    }

    @GetMapping("/fornecedor")
    public ResponseEntity<Resource> reportFornecedores() {
        byte[] reportContent;
        try {
            reportContent = jasperReportService.getRelatorio("fornecedor", "Fornecedores");
        } catch (JRException | IOException e) {
            System.out.println("Error generating report: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
        // System.out.println(resource+" resource");

        return sendFile(reportContent, "fornecedores.pdf");
    }

    @GetMapping("/aniversario")
    public ResponseEntity<Resource> reportAniversariantes() {
        byte[] reportContent;
        try {
            reportContent = jasperReportService.getRelatorio("aniversario", "Aniversariantes");
        } catch (JRException e) {
            System.out.println("Erro de compilação: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }catch (IOException e) {
            System.out.println("Erro IO: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
        // System.out.println(resource+" resource");

        return sendFile(reportContent, "aniversariantes.pdf");
    }

    @GetMapping("/consumocliente")
    public ResponseEntity<Resource> reportConsumoPorUsuario() {
        byte[] reportContent;
        List<ConsumoClienteDto> consumoClienteDto = clienteRepository.findByConsumoTotalPorCliente();
        System.out.println(consumoClienteDto.get(0));
        try {
            reportContent = jasperReportService.getRelatorio("consumoporcliente", "Consumo Total por Cliente");
        } catch (JRException e) {
            System.out.println("Erro de compilação: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }catch (IOException e) {
            System.out.println("Erro IO: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
        // System.out.println(resource+" resource");

        return sendFile(reportContent, "consumo_cliente.pdf");
    }
    
    @GetMapping("/clientenegativo")
    public ResponseEntity<Resource> reportClientesNegativos() {
        byte[] reportContent;
        try {
            reportContent = jasperReportService.getRelatorio("clientenegativado", "Clientes Negativados");
        } catch (JRException e) {
            System.out.println("Erro de compilação: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }catch (IOException e) {
            System.out.println("Erro IO: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
        // System.out.println(resource+" resource");

        return sendFile(reportContent, "cliente_negativado.pdf");
    }

    @GetMapping("/consumodiarioporcliente")
    public ResponseEntity<Resource> reportConsumoDiarioPorCliente() {
        byte[] reportContent;
        try {
            reportContent = jasperReportService.getRelatorio("consumoDiarioPorCliente&Total", "Consumo Diário por Cliente");
            System.out.println("\n\n\n--Relatório gerado com sucesso--\n"+reportContent.length+" bytes\n\n\n");
        } catch (JRException e) {
            System.out.println("Erro de compilação: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }catch (IOException e) {
            System.out.println("Erro IO: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
        // System.out.println(resource+" resource");

        return sendFile(reportContent, "consumoPorCliente&Total.pdf");
    }

    @GetMapping("/dre")
    public ResponseEntity<Resource> reportDre() {
        byte[] reportContent;
        try {
            reportContent = jasperReportService.getRelatorio("dre", "DRE Diario");
            System.out.println("\n\n\n--Relatório gerado com sucesso--\n"+reportContent.length+" bytes\n\n\n");
        } catch (JRException e) {
            System.out.println("Erro de compilação: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }catch (IOException e) {
            System.out.println("Erro IO: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
        // System.out.println(resource+" resource");

        return sendFile(reportContent, "dre.pdf");
    }
}
