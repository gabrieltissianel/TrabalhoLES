package com.jga.les.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jga.les.model.Pagamento;
import com.jga.les.service.GenericService;

@RestController
@RequestMapping("/pagamento")
public class PagamentoController extends GenericController<Pagamento>{
    
    public PagamentoController(GenericService<Pagamento> genericApplication){
        super(genericApplication);
    }

}
