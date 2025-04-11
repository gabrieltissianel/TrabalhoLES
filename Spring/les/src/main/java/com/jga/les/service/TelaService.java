package com.jga.les.service;

import com.jga.les.model.Tela;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


@Service
public class TelaService extends GenericService<Tela> {

    public TelaService(JpaRepository<Tela, Long> objRepository) {
        super(objRepository, Tela.class);
    }

}