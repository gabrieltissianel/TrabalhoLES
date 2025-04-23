package com.jga.les.service;

import com.jga.les.model.HistoricoProdutos;
import com.jga.les.model.Produto;
import com.jga.les.repository.HistoricoProdutosRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class HistoricoProdutosService extends GenericService<HistoricoProdutos> {

    public HistoricoProdutosService(JpaRepository<HistoricoProdutos, Long> objRepository) {
        super(objRepository, HistoricoProdutos.class);
    }

    public ResponseEntity<List<HistoricoProdutos>> findByProduto(Long produto_id) {
        return ResponseEntity.ok(((HistoricoProdutosRepository) objRepository).findByProdutoId(produto_id));
    }
}