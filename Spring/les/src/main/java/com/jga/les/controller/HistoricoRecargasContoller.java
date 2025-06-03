package com.jga.les.controller;

import com.jga.les.model.HistoricoRecarga;
import com.jga.les.service.GenericService;

import com.jga.les.service.HistoricoRecargasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historicorecargas")
public class HistoricoRecargasContoller extends GenericController<HistoricoRecarga, Long> {

    public HistoricoRecargasContoller(GenericService<HistoricoRecarga, Long> genericApplication) {
        super("/recargas", genericApplication);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<HistoricoRecarga>> getByClienteId(@PathVariable Long clienteId) {
        return ResponseEntity.ok(((HistoricoRecargasService) genericService).getByClienteId(clienteId));
    }

}