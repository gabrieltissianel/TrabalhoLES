package com.jga.les.service;

import com.jga.les.model.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ClienteService extends GenericService<Cliente> {

    public ClienteService(JpaRepository<Cliente, Long> objRepository) {
        super(objRepository, Cliente.class);
    }
}