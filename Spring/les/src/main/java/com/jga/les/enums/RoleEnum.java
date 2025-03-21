package com.jga.les.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
    INSERIR("inserir"),
    ALTERAR("alterar"),
    EXCLUIR("excluir");

    private String role;
    
    RoleEnum(String string) {
        this.role = string;
    }
}
