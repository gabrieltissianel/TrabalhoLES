package com.jga.les.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jga.les.dtos.LoginDto;
import com.jga.les.dtos.UsuarioDto;
import com.jga.les.model.Usuario;
import com.jga.les.repository.UsuarioRepository;



@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private AuthenticationManager authenticationManager;    
    
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UsuarioDto> autenticacao(@RequestBody LoginDto loginDto){
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.login(), loginDto.senha()));    

        Usuario us = usuarioRepository.findByLogin(loginDto.login());
        UsuarioDto usuarioDto = new UsuarioDto(us.getId(), us.getNome(), us.getLogin());
        return ResponseEntity.ok().body(usuarioDto);
    }
}
