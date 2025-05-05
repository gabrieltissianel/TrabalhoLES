package com.jga.les.controller;

import com.jga.les.model.Fornecedor;
import com.jga.les.service.GenericService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/fornecedor")
public class FornecedorController extends GenericController<Fornecedor, Long> {

    public FornecedorController(GenericService<Fornecedor, Long> genericApplication) {
        super("/fornecedor", genericApplication);
    }
}