package com.jga.les.controller;

import com.jga.les.model.HistoricoProdutos;
import com.jga.les.service.GenericService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/historicoprodutos")
public class HistoricoProutosContoller extends GenericController<HistoricoProdutos> {

    public HistoricoProutosContoller(GenericService<HistoricoProdutos> genericApplication) {
        super(genericApplication);
    }
}