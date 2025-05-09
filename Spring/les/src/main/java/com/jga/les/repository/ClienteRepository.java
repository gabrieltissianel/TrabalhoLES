package com.jga.les.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jga.les.dtos.ConsumoClienteDto;
import com.jga.les.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @Query("SELECT c FROM Cliente c WHERE EXTRACT(DAY FROM c.dt_nascimento)+1 = EXTRACT(DAY FROM CURRENT_DATE) AND EXTRACT(MONTH FROM c.dt_nascimento) = EXTRACT(MONTH FROM CURRENT_DATE)")
    List<Cliente> findByAniversario();

    @Query(value = "SELECT cli.id, cli.nome, SUM(CP.preco) FROM Cliente cli RIGHT JOIN Compra com on com.cliente_id=cli.id LEFT JOIN compra_produto CP ON CP.compra_id = com.id GROUP BY CLI.ID", nativeQuery = true)
    List<ConsumoClienteDto> findByConsumoTotalPorCliente();

    @Query(value = "SELECT * FROM CLIENTE C WHERE C.ultimo_dia_negativado is not null or C.saldo<0", nativeQuery = true)
    List<Cliente> findByClientesDevedores();
}