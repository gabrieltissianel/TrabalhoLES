package com.jga.les.service;

import com.jga.les.model.CompraProduto;
import com.jga.les.model.CompraProdutoKey;
import com.jga.les.repository.CompraProdutoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CompraProdutoService extends GenericService<CompraProduto, CompraProdutoKey> {
    @Autowired
    ProdutoService produtoService;
    @Autowired
    CompraService compraService;

    public CompraProdutoService(JpaRepository<CompraProduto, CompraProdutoKey> objRepository) {
        super(objRepository, CompraProduto.class);
    }

    public ResponseEntity<CompraProduto> findByCompraProdutoKey(CompraProdutoKey compraProdutoKey) {
        return ResponseEntity.ok(((CompraProdutoRepository) objRepository).findById(compraProdutoKey).get());
    }   

    @Override
    public ResponseEntity<CompraProduto> add(CompraProduto obj)
            throws IllegalArgumentException, OptimisticLockingFailureException {
        if(obj.getId().getIdcompra() == 0 || obj.getId().getIdproduto() == 0){
            return ResponseEntity.badRequest().build();
        }
        // Verifica se a quantidade está definida
        if(obj.getQntd() == null){
            obj.setQntd(1.0);;
        }
        // Verifica se o produto está definido
        if(obj.getProduto() == null){
            try {
                obj.setProduto(produtoService.findById(obj.getId().getIdproduto()).getBody());
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        }
        // Verifica se a compra está definida
        if(obj.getCompra() == null){
            try {
                obj.setCompra(compraService.findById(obj.getId().getIdcompra()).getBody());
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        }
        //Verifica se o preço e o custo estão definidos 
        if(obj.getPreco() == null){
            try {
                obj.setPreco(produtoService.findById(obj.getId().getIdproduto()).getBody().getPreco());
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        }
        if(obj.getCusto() == null){
            try {
                obj.setCusto(produtoService.findById(obj.getId().getIdproduto()).getBody().getCusto());
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        }
        //Atualiza a quantidade do produto na compra
        try {
            ResponseEntity<CompraProduto> cp = this.findByCompraProdutoKey(obj.getId());
            obj.setQntd(cp.getBody().getQntd() + obj.getQntd());
            return update(obj, cp.getBody().getId());
        } catch (Exception e) {
            return super.add(obj);
        }
    }

    @Override
    public ResponseEntity<String> remove(CompraProdutoKey id) {
        System.out.println(id.getIdcompra());
        return super.remove(id);
    }
}