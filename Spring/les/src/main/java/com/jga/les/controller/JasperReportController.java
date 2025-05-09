package com.jga.les.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.jga.les.service.JasperReportService;

import net.sf.jasperreports.engine.JRException;


@RestController
@RequestMapping("/jasper")
public class JasperReportController {
    @Autowired
    JasperReportService jasperReportService;

    @Autowired
    FornecedorRepository fornecedorRepository;
    @Autowired
    ClienteRepository clienteRepository;

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
            reportContent = jasperReportService.getRelatorio(fornecedorRepository.findAll(), "fornecedor", "Fornecedores");
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
            reportContent = jasperReportService.getRelatorio(clienteRepository.findAll(), "aniversario", "Aniversariantes");
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
            reportContent = jasperReportService.getRelatorio(clienteRepository.findByConsumoTotalPorCliente(), "consumoporcliente", "Consumo Total por Cliente");
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
}
