package com.jga.les.controller;

import com.jga.les.model.HistoricoProdutos;
import com.jga.les.model.Produto;
import com.jga.les.service.GenericService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produto")
public class ProdutoContoller extends GenericController<Produto> {
    @Autowired
    HistoricoProutosContoller historicoProutosContoller;
    
    HistoricoProdutos historicoProdutos;

    public ProdutoContoller(GenericService<Produto> genericApplication) {
        super("/produto", genericApplication);
    }

    @Override
    public ResponseEntity<Produto> edit(long id, @Valid Produto obj) {
        ResponseEntity<HistoricoProdutos> result = updateHistorico(obj);
        if(result != null){
            return super.edit(id, obj);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(obj);
    }

    @Override
    public ResponseEntity<Produto> add(@Valid Produto obj) {
        ResponseEntity<HistoricoProdutos> result = updateHistorico(obj);
        if(result != null){
            return super.add(obj);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(obj);
    }

    private ResponseEntity<HistoricoProdutos> updateHistorico(Produto obj){
        historicoProdutos = new HistoricoProdutos();

        historicoProdutos.setCusto_novo(obj.getCusto());
        historicoProdutos.setPreco_novo(obj.getPreco());
        historicoProdutos.setProduto(obj);

        return historicoProutosContoller.add(historicoProdutos);        
    }
}