package com.jga.les.service;

import com.jga.les.model.HistoricoRecarga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class HistoricoRecargasService extends GenericService<HistoricoRecarga, Long> {

    public HistoricoRecargasService(JpaRepository<HistoricoRecarga, Long> objRepository) {
        super(objRepository, HistoricoRecarga.class);
    }
}