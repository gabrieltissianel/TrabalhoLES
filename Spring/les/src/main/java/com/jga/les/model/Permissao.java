package com.jga.les.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Data;

@Data
@Entity
public class Permissao {
    @EmbeddedId
    private PermissaoKey id;

    @ManyToOne
    @MapsId("idusuario")
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @MapsId("idtela")
    @JoinColumn(name = "tela_id")
    private Tela tela;

    private boolean inclusao;

    private boolean alteracao;

    private boolean exclusao;
}
