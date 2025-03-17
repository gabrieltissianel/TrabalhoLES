package com.jga.les.service;

import com.jga.les.model.HistoricoProdutos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class HistoricoProdutosService extends GenericService<HistoricoProdutos> {

    public HistoricoProdutosService(JpaRepository<HistoricoProdutos, Long> objRepository) {
        super(objRepository, HistoricoProdutos.class);
    }
}