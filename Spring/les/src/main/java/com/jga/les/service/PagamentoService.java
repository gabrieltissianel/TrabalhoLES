package com.jga.les.service;

import com.jga.les.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class PagamentoService extends GenericService<Pagamento> {

    public PagamentoService(JpaRepository<Pagamento, Long> objRepository) {
        super(objRepository, Pagamento.class);
    }
}