package com.jga.les.service;

import com.jga.les.model.Cliente;
import com.jga.les.model.Compra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CompraService extends GenericService<Compra, Long> {
    @Autowired
    ClienteService clienteService;

    public CompraService(JpaRepository<Compra, Long> objRepository) {
        super(objRepository, Compra.class);
    }

    @Override
    public ResponseEntity<Compra> add(Compra obj) throws IllegalArgumentException, OptimisticLockingFailureException {
        Cliente cliente = obj.getCliente();
        Compra compraAberta = ((ClienteService) clienteService).findCompraAberta(cliente.getId()).getBody();
        if (compraAberta != null) {
            // Se já existe uma compra aberta, não permite a criação de uma nova
            throw new RuntimeException("Cliente já possui uma compra aberta.");
        }
        return super.add(obj);
    }

    public Compra concluir(Long id) {
        Compra compra = objRepository.findById(id).orElseThrow(() -> new RuntimeException("Compra nao existente."));
        if (compra.getSaida() == null) {

            Date hoje = new Date();

            Cliente cliente = compra.getCliente();
            cliente.setSaldo(cliente.getSaldo() - compra.getTotal());
            if (cliente.getSaldo() < 0 && cliente.getUltimo_dia_negativado() == null) {
                cliente.setUltimo_dia_negativado(hoje);
            }
            clienteService.update(cliente, cliente.getId());

            compra.setCliente(cliente);
            compra.setSaida(hoje);
            objRepository.save(compra);
            return compra;

        } else {
            throw new RuntimeException("Compra ja concluida.");
        }
    }

}