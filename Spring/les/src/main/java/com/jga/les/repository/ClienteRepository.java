package com.jga.les.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jga.les.dtos.ClienteDto;
import com.jga.les.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @Query("SELECT c FROM Cliente c WHERE EXTRACT(DAY FROM c.dt_nascimento) = EXTRACT(DAY FROM CURRENT_DATE) AND EXTRACT(MONTH FROM c.dt_nascimento) = EXTRACT(MONTH FROM CURRENT_DATE)")
    List<ClienteDto> findByAniversario();
}