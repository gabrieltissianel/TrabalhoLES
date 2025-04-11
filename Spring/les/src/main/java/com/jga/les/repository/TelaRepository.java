package com.jga.les.repository;

import com.jga.les.model.Tela;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TelaRepository extends JpaRepository<Tela, Long> {
    Optional<Tela> findByNome(String nome);
}