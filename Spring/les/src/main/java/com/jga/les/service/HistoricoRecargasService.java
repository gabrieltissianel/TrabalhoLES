package com.jga.les.service;

import com.jga.les.model.HistoricoRecarga;

import com.jga.les.repository.HistoricoRecargasRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoricoRecargasService extends GenericService<HistoricoRecarga, Long> {

    public HistoricoRecargasService(JpaRepository<HistoricoRecarga, Long> objRepository) {
        super(objRepository, HistoricoRecarga.class);
    }

    public List<HistoricoRecarga> getByClienteId(Long clienteId) {
        return ((HistoricoRecargasRepository)objRepository).getHistoricoRecargasByCliente_Id(clienteId);
    }
}