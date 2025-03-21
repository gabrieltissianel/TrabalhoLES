package com.jga.les.exceptions;


public class AccessDenied extends Exception{
    public AccessDenied(){
        super("Acesso Negado!\nLogin ou Senha errada.");
    }
}
