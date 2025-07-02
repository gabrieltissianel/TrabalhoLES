package com.jga.les.repository;

import com.jga.les.model.Cliente;
import com.jga.les.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
    @Query(value = """
    SELECT cliente_id, AVG(total_compra)
    FROM (
        SELECT c.id AS compra_id, c.cliente_id, SUM(cp.qntd * cp.preco) AS total_compra
        FROM compra c
        JOIN compra_produto cp ON c.id = cp.compra_id
        WHERE CAST(c.saida AS date) BETWEEN CAST(:dataInicio AS date) AND CAST(:dataFim AS date)
        GROUP BY c.id, c.cliente_id
    ) AS subquery
    GROUP BY cliente_id
    """, nativeQuery = true)
    List<Object[]> getTicketMedioMultiplosClientes(
            @Param("dataInicio") Date dataInicio,
            @Param("dataFim") Date dataFim); //corrigido Gabriel

    // VENDAS POR DIA (PostgreSQL compatible)
    @Query("SELECT c.nome, SUM(cp.qntd * cp.preco), v.saida " +
            "FROM Compra v " +
            "JOIN v.cliente c " +
            "JOIN v.compraProdutos cp " +  // Acesso aos produtos da compra
            "WHERE CAST(v.saida AS date) = CAST(:data AS date) " +
            "GROUP BY c.nome, v.saida")
    List<Object[]> findVendasPorDia(@Param("data") Date data); //corrigido Gabriel

    @Query("""
        SELECT\s
        c.nome,\s
        SUM(cp.qntd * cp.preco) AS total_dia
        FROM Compra v\s
        JOIN v.cliente c\s
        JOIN v.compraProdutos cp\s
        WHERE CAST(v.saida AS DATE) = CAST(:data AS DATE)
        GROUP BY c.nome
    """)
    List<Object[]> consumoPorDia(@Param("data") Date data);

    // ÚLTIMA VENDA POR CLIENTE (PostgreSQL compatible)
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
        WHERE v.id = (
            SELECT id 
            FROM compra 
            WHERE cliente_id = :idCliente 
            ORDER BY saida DESC 
            LIMIT 1
        )
        GROUP BY v.id, v.cliente_id, c.nome, v.saida
    """, nativeQuery = true)
    List<Object[]> getUltimaVenda(@Param("idCliente") long idCliente); //corrigido

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

    
    @Query(nativeQuery = true, value = """

            WITH datas AS (
        SELECT generate_series(
                       CAST(:dataInicio AS DATE),
                       CAST(:dataFim AS DATE),
                       INTERVAL '1 day'
               )::DATE AS dia
    )
    SELECT
        d.dia,
        COALESCE(SUM(hr.valor), 0) AS total_recargas,
        COALESCE(SUM(p.valor), 0) AS total_pagamentos,
        COALESCE(SUM(hr.valor), 0) - COALESCE(SUM(p.valor), 0) AS saldo_diario,
        SUM(COALESCE(SUM(hr.valor), 0) - COALESCE(SUM(p.valor), 0)) OVER (
            ORDER BY d.dia
            ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
            ) AS saldo_acumulado
    FROM
        datas d
            LEFT JOIN historico_recarga hr ON CAST(hr.data AS DATE) = d.dia
            LEFT JOIN pagamento p ON CAST(p.dt_vencimento AS DATE) = d.dia
    GROUP BY
        d.dia
    ORDER BY
        d.dia;
    """)
    List<Object[]> findDreDiario(
        @Param("dataInicio") Date dataInicio, 
        @Param("dataFim") Date dataFim); // vsfd Igor

    @Query(nativeQuery = true, value = """
    SELECT
        (SELECT COALESCE(SUM(hr.valor), 0)
         FROM historico_recarga hr
         WHERE hr.data <= :data) -
    
        (SELECT COALESCE(SUM(p.valor), 0)
         FROM pagamento p
         WHERE p.dt_pagamento <= :data) AS saldo_atual;
    """)
    double findAcumulado(
            @Param("data") Date data);
}