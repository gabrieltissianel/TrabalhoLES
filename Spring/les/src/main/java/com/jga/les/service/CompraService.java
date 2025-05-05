package com.jga.les.service;

import com.jga.les.model.Compra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CompraService extends GenericService<Compra> {
    public CompraService(JpaRepository<Compra, Long> objRepository) {
        super(objRepository, Compra.class);
    }
}