package com.jga.les.service;

import com.jga.les.model.Produto;
import com.jga.les.repository.ProdutoRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService extends GenericService<Produto, Long> {

    public ProdutoService(JpaRepository<Produto, Long> objRepository) {
        super(objRepository, Produto.class);
    }

    public Produto findByCodigo(String codigo) {
        return ((ProdutoRepository) objRepository).findByCodigo(codigo);
    }
}