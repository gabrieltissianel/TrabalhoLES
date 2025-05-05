package com.jga.les.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jga.les.model.CompraProduto;
import com.jga.les.model.CompraProdutoKey;

public interface CompraProdutoRepository extends JpaRepository<CompraProduto, CompraProdutoKey> {
    // Custom query methods can be added here if needed
    // For example, to find CompraProduto by Compra or Produto
    // List<CompraProduto> findByCompra(Compra compra);
    // List<CompraProduto> findByProduto(Produto produto);
}