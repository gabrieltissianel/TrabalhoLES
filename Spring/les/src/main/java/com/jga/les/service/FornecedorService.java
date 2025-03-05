package com.jga.les.service;

import com.jga.les.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class FornecedorService extends GenericService<Fornecedor> {

    public FornecedorService(JpaRepository<Fornecedor, Long> objRepository) {
        super(objRepository, Fornecedor.class);
    }
}