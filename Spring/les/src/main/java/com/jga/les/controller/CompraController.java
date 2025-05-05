package com.jga.les.controller;

import com.jga.les.model.Compra;
import com.jga.les.service.GenericService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compra")
public class CompraController extends GenericController<Compra, Long> {

    public CompraController(GenericService<Compra, Long> genericApplication) {
        super("/compra", genericApplication);
    }
}