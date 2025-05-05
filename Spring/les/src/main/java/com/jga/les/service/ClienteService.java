package com.jga.les.service;

import com.jga.les.dtos.ClienteDto;
import com.jga.les.model.Cliente;
import com.jga.les.repository.ClienteRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClienteService extends GenericService<Cliente> {

    public ClienteService(JpaRepository<Cliente, Long> objRepository) {
        super(objRepository, Cliente.class);
    }

    public ResponseEntity<List<ClienteDto>> findByAniversario(){
        return ResponseEntity.ok(((ClienteRepository)objRepository).findByAniversario());
    }
}