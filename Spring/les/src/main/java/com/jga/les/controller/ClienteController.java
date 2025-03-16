package com.jga.les.controller;

import com.jga.les.model.Cliente;
import com.jga.les.service.GenericService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
public class ClienteController extends GenericController<Cliente> {

    public ClienteController(GenericService<Cliente> genericApplication) {
        super(genericApplication);
    }
}