package com.jga.les.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jga.les.model.CompraProduto;
import com.jga.les.model.CompraProdutoKey;
import com.jga.les.service.CompraProdutoService;
import com.jga.les.service.GenericService;
import com.jga.les.service.ProdutoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/compraproduto")
public class CompraProdutoController extends GenericController<CompraProduto, CompraProdutoKey> {
    @Autowired
    ProdutoService produtoService;

    public CompraProdutoController(GenericService<CompraProduto, CompraProdutoKey> genericApplication) {
        super("/compraproduto", genericApplication);
    }

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<CompraProduto> add(@Valid CompraProduto obj) {
        // Verifica se o id do produto e da compra são válidos
        if(obj.getId().getIdcompra() == 0 || obj.getId().getIdproduto() == 0){
            return ResponseEntity.badRequest().build();
        }
        // Verifica se a quantidade está definida
        if(obj.getQntd() == null){
            obj.setQntd(1);
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

        try {
            ResponseEntity<CompraProduto> cp = ((CompraProdutoService) genericService).findByCompraProdutoKey(obj.getId());
            obj.setQntd(cp.getBody().getQntd() + obj.getQntd());
            return edit(cp.getBody().getId(), obj);
        } catch (Exception e) {
            return super.add(obj);
        }
    }
}