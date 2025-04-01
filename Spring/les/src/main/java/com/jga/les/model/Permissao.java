package com.jga.les.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Permissao{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Tela tela;

    private boolean add;
    private boolean edit;
    private boolean delete;
}

