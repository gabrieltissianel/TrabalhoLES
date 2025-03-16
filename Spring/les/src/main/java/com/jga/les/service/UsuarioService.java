package com.jga.les.service;

import com.jga.les.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService extends GenericService<Usuario> {

    public UsuarioService(JpaRepository<Usuario, Long> objRepository) {
        super(objRepository, Usuario.class);
    }
}