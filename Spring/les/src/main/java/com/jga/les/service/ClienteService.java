package com.jga.les.service;
import com.jga.les.model.Cliente;
import com.jga.les.model.Compra;
import com.jga.les.repository.ClienteRepository;

import java.util.List;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClienteService extends GenericService<Cliente, Long> {

    public ClienteService(JpaRepository<Cliente, Long> objRepository) {
        super(objRepository, Cliente.class);
    }

    public ResponseEntity<Compra> findCompraAberta(Long id) {
        return ResponseEntity.ok(((ClienteRepository)objRepository).findCompraAberta(id));
    }

    public ResponseEntity<List<Cliente>> findByAniversario(){
        return ResponseEntity.ok(((ClienteRepository)objRepository).findByAniversario());
    }

    @Override
    public ResponseEntity<Cliente> update(Cliente obj, Long id) {
        if (obj.getSaldo() >=  0 && obj.getUltimo_dia_negativado() != null) {
            obj.setUltimo_dia_negativado(null);
        }
        return super.update(obj, id);
    }

    @Override
    public ResponseEntity<Cliente> add(Cliente obj) throws IllegalArgumentException, OptimisticLockingFailureException {
        obj.setDt_nascimento(obj.getDt_nascimento());
        return super.add(obj);
    }
}