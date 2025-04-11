package com.jga.les.controller;

import com.jga.les.model.HistoricoRecarga;
import com.jga.les.service.GenericService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/historicorecargas")
public class HistoricoRecargasContoller extends GenericController<HistoricoRecarga> {

    public HistoricoRecargasContoller(GenericService<HistoricoRecarga> genericApplication) {
        super("/recargas", genericApplication);
    }
}