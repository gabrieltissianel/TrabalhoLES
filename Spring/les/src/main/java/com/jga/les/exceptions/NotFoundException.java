package com.jga.les.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String className){
        super(className + " não encontrado.");
    }
}