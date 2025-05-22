package com.jga.les.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jga.les.device.TMT20XService;

@RestController
@RequestMapping("/tmt20x")
public class TMT20XController {
    @Autowired
    private TMT20XService tmt20xService;

    @PostMapping("/teste")
    public ResponseEntity<String> teste() {
        try {
            tmt20xService.teste();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao listar impressoras: " + e.getMessage());
        }
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/cartao/{cartao}")
    @PreAuthorize("hasAuthority(#root.this.getNomeTela(''))")
    public ResponseEntity<String> informacoesDoCliente(@PathVariable String cartao) {
        try {
            tmt20xService.imprimirComprovanteCompra(cartao);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao gerar informacoes do cliente: " + e.getMessage());
        }
        return ResponseEntity.ok("ok");
    }
}