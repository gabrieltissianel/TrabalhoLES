package com.jga.les.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Schema.Printer;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jga.les.device.PrinterDL200Device;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/printerdl200")
public class PrinterDL200Controller {
    
    private PrinterDL200Device printerDL200Device;

    PrinterDL200Controller(PrinterDL200Device printerDL200Device) {
        this.printerDL200Device = printerDL200Device;
    }

    @PostMapping("/barcode")
    @PreAuthorize("hasAuthority(#root.this.getNomeTela(''))")
    public ResponseEntity<String> teste(@RequestParam String data, @RequestParam PrinterDL200Device.BarcodeType type) {
        System.out.println("Imprimindo código de barras: " + data + " com tipo: " + type);
        try {
            printerDL200Device.printBarcode(data, type);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao imprimir código de barras: " + e.getMessage());
        }
        return ResponseEntity.ok("Código de barras impresso com sucesso");
    }
}
