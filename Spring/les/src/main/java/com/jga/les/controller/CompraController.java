package com.jga.les.controller;

import com.jga.les.model.Compra;
import com.jga.les.service.CompraService;
import com.jga.les.service.GenericService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compra")
public class CompraController extends GenericController<Compra, Long> {

    public CompraController(GenericService<Compra, Long> genericApplication) {
        super("/compra", genericApplication);
    }

    @PutMapping("/concluir/{id}")
    public ResponseEntity<Compra> concluirCompra(@PathVariable Long id) {
        return ResponseEntity.ok( ((CompraService)genericService).concluir(id) );
    }
}