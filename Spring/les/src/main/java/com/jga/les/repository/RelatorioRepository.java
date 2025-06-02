package com.jga.les.repository;

import com.jga.les.model.Cliente;
import com.jga.les.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface RelatorioRepository extends JpaRepository<Compra, Long>{

    // ANIVERSARIANTES DO DIA (PostgreSQL compatible)
    @Query("SELECT c FROM Cliente c " +
            "WHERE EXTRACT(DAY FROM c.dt_nascimento) = EXTRACT(DAY FROM CURRENT_DATE) " +
            "AND EXTRACT(MONTH FROM c.dt_nascimento) = EXTRACT(MONTH FROM CURRENT_DATE)")
    List<Cliente> findAniversariantesDoDia(); //corrigido Gabriel

    // ANIVERSARIANTES DO MÊS (PostgreSQL compatible)
    @Query("SELECT c FROM Cliente c " +
            "WHERE EXTRACT(MONTH FROM c.dt_nascimento) = :mes")
    List<Cliente> findAniversarianteMes(@Param("mes") int mes); //corrigido Gabriel

    // TICKET MÉDIO POR PERÍODO (PostgreSQL compatible)
    @Query("SELECT v.cliente.id,  AVG(SUM(cp.qntd * cp.preco)) " +
            "FROM Compra v JOIN v.compraProdutos cp " +
            "WHERE CAST(v.saida AS date) BETWEEN CAST(:dataInicio AS date) AND CAST(:dataFim AS date) " +
            "GROUP BY v.cliente.id")
    List<Object[]> getTicketMedioMultiplosClientes(
            @Param("dataInicio") Date dataInicio,
            @Param("dataFim") Date dataFim); //corrigido Gabriel

    // VENDAS POR DIA (PostgreSQL compatible)
    @Query("SELECT c.nome, SUM(cp.qntd * cp.preco), CAST(v.saida AS date) " +
            "FROM Compra v " +
            "JOIN v.cliente c " +
            "JOIN v.compraProdutos cp " +  // Acesso aos produtos da compra
            "WHERE CAST(v.saida AS date) = CAST(:data AS date) " +
            "GROUP BY c.nome, CAST(v.saida AS date)")
    List<Object[]> findVendasPorDia(@Param("data") Date data); //corrigido Gabriel

    // ÚLTIMA VENDA POR CLIENTE (PostgreSQL compatible)
    @Query("SELECT v.id, v.cliente, v.saida, AVG(SUM(cp.qntd * cp.preco)) " +
            "FROM Compra v JOIN v.compraProdutos cp " +
            "WHERE v.cliente = :idCliente " +
            "ORDER BY v.saida DESC LIMIT 1")
    Object[] getUltimaVenda(@Param("idCliente") long idCliente); //corrigido

    // CLIENTES ENDIVIDADOS (mantido)
    @Query("SELECT c FROM Cliente c WHERE c.saldo < 0")
    List<Cliente> findClientesEndividados(); //corrigido Gabriel

    // Consulta nativa otimizada para última venda por cliente
    @Query(value = """
        SELECT 
            v.id AS venda_id, 
            v.cliente_id, 
            c.nome AS cliente_nome, 
            v.saida, 
            COALESCE(SUM(cp.qntd * cp.preco), 0) AS valor_total
        FROM compra v
        JOIN cliente c ON v.cliente_id = c.id
        LEFT JOIN compra_produto cp ON cp.compra_id = v.id
        WHERE (v.cliente_id, v.saida) IN (
            SELECT cliente_id, MAX(saida) 
            FROM compra
            GROUP BY cliente_id
        )
        GROUP BY v.id, v.cliente_id, c.nome, v.saida
        ORDER BY c.nome
    """, nativeQuery = true)
    List<Object[]> findUltimaVendaTodosClientes(); //corrigido Deepseek


    @Query("""
    SELECT p.codigo, p.nome, p.preco, SUM(cp.qntd)
    FROM CompraProduto cp
    JOIN cp.produto p
    JOIN cp.compra v
    WHERE p.unitario
    AND CAST(v.saida AS date) BETWEEN CAST(:dataInicio AS date) AND CAST(:dataFim AS date)
    GROUP BY p.codigo, p.nome, p.preco
    """)
    List<Object[]> findProdutosSerialVendidosPorPeriodo( //corrigido gabriel
            @Param("dataInicio") Date dataInicio,
            @Param("dataFim") Date dataFim);


    @Query(nativeQuery = true, value = """
    WITH dias AS (
        SELECT generate_series(
            CAST(:dataInicio AS date),
            CAST(:dataFim AS date),
            '1 day'::interval
        )::date as dia
    ),
    vendas_com_total AS (
        SELECT 
            v.id,
            CAST(v.saida AS date) as saida_date,
            COALESCE(SUM(cp.qntd * cp.preco), 0) AS valor_total
        FROM compra v
        LEFT JOIN compra_produto cp ON v.id = cp.compra_id
        GROUP BY v.id, saida_date
    )
    SELECT d.dia, COALESCE(SUM(v.valor_total), 0.0)
    FROM dias d
    LEFT JOIN vendas_com_total v ON v.saida_date = d.dia
    GROUP BY d.dia
    ORDER BY d.dia
    """)
    List<Double> findConsumoDiario(
            @Param("dataInicio") Date dataInicio,
            @Param("dataFim") Date dataFim);

    @Query("SELECT c FROM Cliente c WHERE EXTRACT(MONTH FROM c.dt_nascimento) = :mes")
    List<Cliente> findAniversariantesDoMes(@Param("mes") int mes); // corrigido Gabriel

    @Query("SELECT c FROM Cliente c WHERE c.id = :id")
    Cliente findClienteById(@Param("id") Long id);  // corrigido Gabriel

}