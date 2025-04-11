package com.jga.les.controller;

import com.jga.les.dtos.LoginDto;
import com.jga.les.dtos.UsuarioDto;
import com.jga.les.model.Usuario;
import com.jga.les.service.GenericService;

import com.jga.les.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
public class UsuarioController extends GenericController<Usuario> {

    public UsuarioController(GenericService<Usuario> genericApplication) {
        super(genericApplication);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioDto> authenticateUser(@RequestBody LoginDto loginUserDto) {
        return ((UsuarioService) genericService).authenticateUser(loginUserDto);
    }
}