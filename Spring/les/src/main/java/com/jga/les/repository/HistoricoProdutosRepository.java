package com.jga.les.repository;

import com.jga.les.model.HistoricoProdutos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoProdutosRepository extends JpaRepository<HistoricoProdutos, Long> {
    List<HistoricoProdutos> findByProdutoId(Long id);
}