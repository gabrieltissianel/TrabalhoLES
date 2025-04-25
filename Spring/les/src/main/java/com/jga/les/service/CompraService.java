package com.jga.les.service;

import com.jga.les.model.Compra;

import com.jga.les.model.CompraProduto;
import com.jga.les.repository.CompraProdutoRepository;
import com.jga.les.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompraService extends GenericService<Compra> {

    @Autowired
    private CompraProdutoRepository compraProdutoRepository;
    @Autowired
    private ProdutoRepository produtoRepository;

    public CompraService(JpaRepository<Compra, Long> objRepository) {
        super(objRepository, Compra.class);
    }

    @Override
    public ResponseEntity<Compra> add(Compra obj) throws IllegalArgumentException, OptimisticLockingFailureException {
        if (obj.getCompraProdutos() != null && !obj.getCompraProdutos().isEmpty()) {
            obj.getCompraProdutos().forEach(compraProduto -> {
                compraProduto.setId(compraProdutoRepository.save(compraProduto).getId());
            });
        } else {
            obj.setCompraProdutos(new ArrayList<>());
        }

        return super.add(obj);
    }

    @Override
    public ResponseEntity<String> remove(long id) {
        Optional<Compra> obj = objRepository.findById(id);
        obj.ifPresent(compra -> compra.getCompraProdutos().forEach(compraProduto -> {
            objRepository.deleteById(compraProduto.getId());
        }));
        return super.remove(id);
    }

    @Override
    public ResponseEntity<Compra> update(Compra obj, long id) {
        objRepository.findById(id).ifPresent(compra -> {

            for (CompraProduto compraProduto : compra.getCompraProdutos()) {
                if (!obj.getCompraProdutos().contains(compraProduto)) {
                    compraProdutoRepository.delete(compraProduto);
                }
            }

            for (CompraProduto compraProduto : obj.getCompraProdutos()) {
                if (compraProduto.getId() == null){
                    compraProduto.setId( compraProdutoRepository.save(compraProduto).getId() );
                } else {
                    compraProdutoRepository.save(compraProduto);
                }
            }

            List<CompraProduto> copia = new ArrayList<>(obj.getCompraProdutos());

            obj.getCompraProdutos().removeIf(compraProduto -> compraProduto.getQntd() <= 0);

            objRepository.save(obj);

            copia.forEach(compraProduto -> {
                if(compraProduto.getQntd() == 0){
                    compraProdutoRepository.delete(compraProduto);
                }
            });
        });
        return super.update(obj, id);
    }
}