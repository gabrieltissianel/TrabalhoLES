package com.jga.les.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jga.les.model.Compra;
import com.jga.les.model.CompraProduto;
import com.jga.les.model.CompraProdutoKey;

public interface CompraProdutoRepository extends JpaRepository<CompraProduto, CompraProdutoKey> {
    // Custom query methods can be added here if needed
    // For example, to find CompraProduto by Compra or Produto
    // List<CompraProduto> findByCompra(Compra compra);
    // List<CompraProduto> findByProduto(Produto produto);

    Optional<CompraProduto> findById(CompraProdutoKey id);

    @Query(value = "SELECT * FROM COMPRA_PRODUTO WHERE COMPRA_ID = ?1", nativeQuery = true)
    Optional<List<CompraProduto>> findByCompraId(long compra);
}