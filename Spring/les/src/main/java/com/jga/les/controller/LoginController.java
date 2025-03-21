package com.jga.les.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jga.les.dtos.LoginDto;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;    
    
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String autenticacao(LoginDto loginDto){
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.login(), loginDto.senha()));    
        return "Acesso autorizado.";
    }
}
