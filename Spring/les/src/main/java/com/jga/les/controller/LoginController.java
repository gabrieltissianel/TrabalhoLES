package com.jga.les.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.jga.les.dtos.LoginDto;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;    
    
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String autenticacao(@RequestBody LoginDto loginDto){
        System.out.println(loginDto.login()+" "+loginDto.senha());
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.login(), loginDto.senha()));    
        return "Acesso autorizado.";
    }
}
