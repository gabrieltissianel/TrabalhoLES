package com.jga.les.repository;

import com.jga.les.model.Cliente;
import com.jga.les.model.HistoricoRecarga;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoRecargasRepository extends JpaRepository<HistoricoRecarga, Long> {

    List<HistoricoRecarga> getHistoricoRecargasByCliente_Id(Long clienteId);
}