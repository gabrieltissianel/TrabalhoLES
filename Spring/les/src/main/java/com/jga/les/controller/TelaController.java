package com.jga.les.controller;

import com.jga.les.model.Tela;
import com.jga.les.service.GenericService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tela")
public class TelaController extends GenericController<Tela, Long> {

    public TelaController(GenericService<Tela, Long> genericApplication) {
        super("/tela", genericApplication);
    }
}