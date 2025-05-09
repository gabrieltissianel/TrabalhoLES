package com.jga.les.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jga.les.model.CompraProduto;
import com.jga.les.model.CompraProdutoKey;
import com.jga.les.service.GenericService;

@RestController
@RequestMapping("/compraproduto")
public class CompraProdutoController extends GenericController<CompraProduto, CompraProdutoKey> {

    public CompraProdutoController(GenericService<CompraProduto, CompraProdutoKey> genericApplication) {
        super("/compraproduto", genericApplication);
    }
}