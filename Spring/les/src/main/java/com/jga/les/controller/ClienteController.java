package com.jga.les.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jga.les.model.Cliente;
import com.jga.les.dtos.*;
import com.jga.les.service.ClienteService;
import com.jga.les.service.GenericService;

@RestController
@RequestMapping("/cliente")
public class ClienteController extends GenericController<Cliente, Long> {

    public ClienteController(GenericService<Cliente, Long> genericApplication) {
        super("/cliente", genericApplication);
    }

    @GetMapping("/aniversario")
    @PreAuthorize("hasAuthority(#root.this.getNomeTela(''))")
    public ResponseEntity<List<ClienteDto>> relatorioAniversarioEntity(@PathVariable long id) {
        return ((ClienteService)genericService).findByAniversario();
    }
}