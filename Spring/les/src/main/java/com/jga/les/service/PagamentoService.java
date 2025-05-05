package com.jga.les.service;

import com.jga.les.model.Pagamento;
import com.jga.les.repository.PagamentoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagamentoService extends GenericService<Pagamento, Long> {

    public PagamentoService(JpaRepository<Pagamento, Long> objRepository) {
        super(objRepository, Pagamento.class);
    }

    public ResponseEntity<List<Pagamento>> findByFornecedor(Long fornecedor_id) {
        return ResponseEntity.ok(((PagamentoRepository) objRepository).findByFornecedorId(fornecedor_id));
    }
}