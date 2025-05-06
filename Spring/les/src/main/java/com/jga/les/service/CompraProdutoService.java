package com.jga.les.service;

import com.jga.les.model.CompraProduto;
import com.jga.les.model.CompraProdutoKey;
import com.jga.les.repository.CompraProdutoRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CompraProdutoService extends GenericService<CompraProduto, CompraProdutoKey> {

    public CompraProdutoService(JpaRepository<CompraProduto, CompraProdutoKey> objRepository) {
        super(objRepository, CompraProduto.class);
    }

    public ResponseEntity<CompraProduto> findByCompraProdutoKey(CompraProdutoKey compraProdutoKey) {
        return ResponseEntity.ok(((CompraProdutoRepository) objRepository).findById(compraProdutoKey).get());

    }   
}