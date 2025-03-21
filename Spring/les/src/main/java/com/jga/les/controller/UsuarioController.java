package com.jga.les.controller;

import com.jga.les.model.Usuario;
import com.jga.les.service.GenericService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
public class UsuarioController extends GenericController<Usuario> {

    public UsuarioController(GenericService<Usuario> genericApplication) {
        super(genericApplication);
    }   
}