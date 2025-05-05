package com.jga.les.service;

import com.jga.les.model.Produto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService extends GenericService<Produto, Long> {

    public ProdutoService(JpaRepository<Produto, Long> objRepository) {
        super(objRepository, Produto.class);
    }
}