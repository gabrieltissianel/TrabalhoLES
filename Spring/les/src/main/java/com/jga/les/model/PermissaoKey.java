package com.jga.les.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PermissaoKey implements Serializable {
    @Column(name = "usuario_id")
    private long  idusuario;
    
    @Column(name = "tela_id")
    private long idtela;
}
