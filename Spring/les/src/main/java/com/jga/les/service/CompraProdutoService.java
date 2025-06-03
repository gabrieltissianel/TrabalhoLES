package com.jga.les.service;

import com.jga.les.model.Compra;
import com.jga.les.model.CompraProduto;
import com.jga.les.model.CompraProdutoKey;
import com.jga.les.repository.CompraProdutoRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CompraProdutoService extends GenericService<CompraProduto, CompraProdutoKey> {

    public CompraProdutoService(JpaRepository<CompraProduto, CompraProdutoKey> objRepository) {
        super(objRepository, CompraProduto.class);
    }

    public ResponseEntity<CompraProduto> findByCompraProdutoKey(CompraProdutoKey compraProdutoKey) {
        return ResponseEntity.ok(((CompraProdutoRepository) objRepository).findById(compraProdutoKey).get());
    }

    public ResponseEntity<CompraProduto> updateWithBody(CompraProduto obj) {
        CompraProdutoRepository repo = (CompraProdutoRepository) objRepository;
        repo.findById(obj.getId()).orElseThrow(() -> new RuntimeException("Produto nao encontrado"));

        if (obj.getQntd() <= 0){
            repo.delete(obj);
            return ResponseEntity.ok(obj);
        }

        return ResponseEntity.ok(repo.save(obj));
    }

    @Override
    public ResponseEntity<String> remove(CompraProdutoKey id) {
        CompraProdutoRepository compraProdutoRepository = (CompraProdutoRepository) objRepository;
        CompraProduto compraProduto = compraProdutoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto nao comprado."));

        if(compraProduto.getProduto().isUnitario()){
            compraProduto.setQntd(compraProduto.getQntd() - 1);
            if(compraProduto.getQntd() <= 0){
                compraProdutoRepository.deleteById(id);
            } else {
                compraProdutoRepository.save(compraProduto);
            }
        } else {
            compraProdutoRepository.deleteById(id);
        }

        return ResponseEntity.ok("Produto Removido.");
    }

    public List<CompraProduto> findByCompra(Compra compra){
        return ((CompraProdutoRepository) objRepository).findByCompraId(compra.getId()).isPresent() ? ((CompraProdutoRepository) objRepository).findByCompraId(compra.getId()).get() : null;
    }
}