package com.jga.les.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jga.les.dtos.ConsumoClienteDto;
import com.jga.les.dtos.ConsumoDiarioClienteDto;
import com.jga.les.model.Cliente;
import com.jga.les.model.Compra;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    //relatorio de aniversariantes
    @Query("SELECT c FROM Cliente c WHERE EXTRACT(DAY FROM c.dt_nascimento)+1 = EXTRACT(DAY FROM CURRENT_DATE) AND EXTRACT(MONTH FROM c.dt_nascimento) = EXTRACT(MONTH FROM CURRENT_DATE)")
    List<Cliente> findByAniversario();

    //relatorio de consumo total de todos os clientes
    @Query(value = "SELECT cli.id, cli.nome, SUM(CP.preco) FROM Cliente cli RIGHT JOIN Compra com on com.cliente_id=cli.id LEFT JOIN compra_produto CP ON CP.compra_id = com.id GROUP BY CLI.ID", nativeQuery = true)
    List<ConsumoClienteDto> findByConsumoTotalPorCliente();

    //relatorio de clientes devedores
    @Query(value = "SELECT * FROM CLIENTE C WHERE C.ultimo_dia_negativado is not null or C.saldo<0", nativeQuery = true)
    List<Cliente> findByClientesDevedores();

    //relatorio de consumo diario por cliente
    @Query(value = "SELECT CLI.ID, CLI.NOME, CAST(COM.entrada AS DATE), SUM(CP.preco), SUM(CP.custo) FROM CLIENTE CLI RIGHT JOIN COMPRA COM ON CLI.ID=COM.cliente_id LEFT JOIN compra_produto CP ON CP.compra_id = COM.id WHERE CLI.ID=?1 GROUP BY COM.entradA, CLI.ID", nativeQuery = true)
    List<ConsumoDiarioClienteDto> findByConsumoPorCliente(Long id);

    @Query(value = "SELECT * FROM COMPRA WHERE cliente_id = ?1 AND saida IS NULL", nativeQuery = true)
    Compra findCompraAberta(Long clienteId);
}