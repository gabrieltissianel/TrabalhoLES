package com.jga.les.repository;

import com.jga.les.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByFornecedorId(Long id);
}