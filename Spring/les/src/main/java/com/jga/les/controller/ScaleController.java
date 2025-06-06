package com.jga.les.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jga.les.device.BalancaDevice;

@RestController
@RequestMapping("/balanca")
public class ScaleController {
    //configuracao da balanca
    //c14 - Prt2
    //c15 - 9600
    //c16 - L
    @Autowired
    private BalancaDevice serialService;

    @GetMapping("/peso")
    public ResponseEntity<Double> getUltimoPeso() {
        Double peso = null;
        try {
            peso = serialService.getUltimoPeso();
        } catch (Exception e) {
             closePort();
             openPort();
            peso = serialService.getUltimoPeso();
        }

        if (peso == null) {
            throw new RuntimeException("Nenhum peso foi lido ainda.");
        }
        return ResponseEntity.ok(peso);
    }

    @PostMapping("/close-port")
    public ResponseEntity<String> closePort() {
        serialService.closePort();
        return ResponseEntity.ok("Porta serial fechada");
    }

    @PostMapping("/open-port")
    public ResponseEntity<String> openPort() {
        if(BalancaDevice.init()) {
            return ResponseEntity.ok("Porta serial aberta com sucesso");
        }
        else {
            return ResponseEntity.status(500).body("Falha ao abrir a porta serial");
        }
    }
    
}