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
            List<CompraProduto> compraProdutos = obj.getCompraProdutos();
            // Criamos uma cópia para evitar ConcurrentModificationException
            List<CompraProduto> copiaProdutos = new ArrayList<>(compraProdutos);

            for (CompraProduto compraProduto : copiaProdutos) {
                if (compraProduto.getProduto().isUnitario()) {
                    verificarProdutoRepetido(compraProdutos, compraProduto);
                }
            }

            for (CompraProduto compraProduto : compra.getCompraProdutos()) {
                if (!obj.getCompraProdutos().contains(compraProduto)) {
                    compraProdutoRepository.delete(compraProduto);
                }
            }

            for (CompraProduto compraProduto : compraProdutos) {
                if (compraProduto.getId() == null){
                    compraProduto.setId( compraProdutoRepository.save(compraProduto).getId() );
                }
            }
        });
        return super.update(obj, id);
    }

    private void verificarProdutoRepetido(List<CompraProduto> produtos, CompraProduto produto) {
        // Filtra produtos unitários com o mesmo ID
        List<CompraProduto> repetidos = produtos.stream()
                .filter(p -> p.getProduto().isUnitario())
                .filter(p -> p.getProduto().getId().equals(produto.getId()))
                .toList();

        // Se houver mais de um item repetido
        if (repetidos.size() > 1) {
            // Mantém o primeiro e remove os demais
            CompraProduto principal = repetidos.getFirst();

            int qtde = 0;
            for (CompraProduto repetido : repetidos) {
                qtde += repetido.getQntd();
            }

            principal.setQntd((double)qtde);

            // Remove todos os repetidos (incluindo o principal)
            produtos.removeAll(repetidos);
            // Adiciona o principal de volta com quantidade atualizada
            produtos.add(principal);
        }

        // Remove itens com quantidade <= 0
        produtos.removeIf(p -> {
            if (p.getQntd() <= 0 ){
                if (p.getId() != null){
                    compraProdutoRepository.findById(p.getId()).ifPresent( produtoD ->
                            compraProdutoRepository.deleteById(produtoD.getId()));
                }
                return true;
            }
            return false;
        });
    }

}