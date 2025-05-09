package com.jga.les.service;

import com.jga.les.model.Cliente;
import com.jga.les.model.Compra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CompraService extends GenericService<Compra, Long> {
    @Autowired
    ClienteService clienteService;

    public CompraService(JpaRepository<Compra, Long> objRepository) {
        super(objRepository, Compra.class);
    }

    public Compra concluir(Long id) {
        Compra compra = objRepository.findById(id).orElseThrow(() -> new RuntimeException("Compra nao existente."));
        if (compra.getSaida() == null) {

            Cliente cliente = compra.getCliente();
            cliente.setSaldo(cliente.getSaldo() - compra.getTotal());
            clienteService.update(cliente, cliente.getId());

            compra.setCliente(cliente);
            compra.setSaida(new Date());
            objRepository.save(compra);
            return compra;

        } else {
            throw new RuntimeException("Compra ja concluida.");
        }
    }

}