package com.jga.les.controller;

import com.jga.les.model.Produto;
import com.jga.les.service.GenericService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produto")
public class ProdutoContoller extends GenericController<Produto> {

    public ProdutoContoller(GenericService<Produto> genericApplication) {
        super(genericApplication);
    }
}