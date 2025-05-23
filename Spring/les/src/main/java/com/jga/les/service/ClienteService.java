package com.jga.les.service;
import com.jga.les.model.Cliente;
import com.jga.les.model.Compra;
import com.jga.les.model.HistoricoRecarga;
import com.jga.les.repository.ClienteRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClienteService extends GenericService<Cliente, Long> {
    private HistoricoRecarga historicoRecarga;
    @Autowired
    private HistoricoRecargasService historicoRecargasService;

    public ClienteService(JpaRepository<Cliente, Long> objRepository) {
        super(objRepository, Cliente.class);
    }

    public ResponseEntity<Compra> findCompraAberta(Long id) {
        return ResponseEntity.ok(((ClienteRepository)objRepository).findCompraAbertaByClienteId(id));
    }

    public ResponseEntity<Cliente> findClienteByCartao(String cartao) {
        Optional<Cliente> cliente = ((ClienteRepository)objRepository).findClienteByCartao(cartao);
        if (cliente.isEmpty()) {
            throw new OptimisticLockingFailureException("Cliente nao encontrado");
        }
        return ResponseEntity.ok(cliente.get());
    }


    public ResponseEntity<Compra> findCompraAberta(String cartao) {
        Optional<Compra> compra = ((ClienteRepository)objRepository).findCompraAbertaByCartao(cartao);
        if (compra.isEmpty()) {
            throw new OptimisticLockingFailureException("Cliente nao possui compras em aberto.");
        }
        return ResponseEntity.ok(compra.get());
    }

    public ResponseEntity<List<Cliente>> findByAniversario(){
        return ResponseEntity.ok(((ClienteRepository)objRepository).findByAniversario());
    }

    public Cliente findByCartao(String cartao) {
        return ((ClienteRepository)objRepository).findByCartao(cartao);
    }
    

    @Override
    public ResponseEntity<Cliente> update(Cliente obj, Long id) {
        if (obj.getSaldo() >=  0 && obj.getUltimo_dia_negativado() != null) {
            obj.setUltimo_dia_negativado(null);
        }
        // historico de recargas
        Cliente cliente = ((ClienteRepository)objRepository).findById(id).get();
        if(cliente.getSaldo() > obj.getSaldo()){
            historicoRecarga = new HistoricoRecarga();
            historicoRecarga.setCliente(obj);
            historicoRecarga.setValor(obj.getSaldo());
            historicoRecarga.setData(new Date()); 
            historicoRecargasService.add(historicoRecarga);
        }
        
        return super.update(obj, id);
    }

    @Override
    public ResponseEntity<Cliente> add(Cliente obj) throws IllegalArgumentException, OptimisticLockingFailureException {
        obj.setDt_nascimento(obj.getDt_nascimento());
        return super.add(obj);
    }
}