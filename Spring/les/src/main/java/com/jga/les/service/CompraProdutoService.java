package com.jga.les.service;

import com.jga.les.model.CompraProduto;
import com.jga.les.model.CompraProdutoKey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CompraProdutoService extends GenericService<CompraProduto, CompraProdutoKey> {

    public CompraProdutoService(JpaRepository<CompraProduto, CompraProdutoKey> objRepository) {
        super(objRepository, CompraProduto.class);
    }
}