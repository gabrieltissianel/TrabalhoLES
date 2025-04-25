package com.jga.les.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jga.les.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @Query("SELECT c FROM Cliente c WHERE FUNCTION('DAY', c.dt_nascimento) = FUNCTION('DAY', CURRENT_DATE) AND FUNCTION('MONTH', c.dt_nascimento) = FUNCTION('MONTH', CURRENT_DATE)")
    List<Cliente> findByAniversario();
}