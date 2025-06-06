package com.jga.les.controller;

import com.jga.les.device.BalancaDevice;
import com.jga.les.model.*;
import com.jga.les.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import com.jga.les.service.CompraProdutoService;
import com.jga.les.service.GenericService;
import com.jga.les.service.ProdutoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/compraproduto")
public class CompraProdutoController extends GenericController<CompraProduto, CompraProdutoKey> {
    @Autowired
    ProdutoService produtoService;

    @Autowired
    CompraService compraService;

    @Autowired
    BalancaDevice balancaService;

    public CompraProdutoController(GenericService<CompraProduto, CompraProdutoKey> genericApplication) {
        super("/compraproduto", genericApplication);
    }


    @PutMapping("/edit")
    public ResponseEntity<CompraProduto> edit (@Valid @RequestBody CompraProduto obj) {
        return ((CompraProdutoService) genericService ).updateWithBody(obj);
    }

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<CompraProduto> add(@Valid @RequestBody CompraProduto obj) {
        Produto produto;
        Compra compra;
        // Verifica se o id do produto e da compra são válidos
        if(obj.getId().getIdcompra() == 0 || obj.getId().getIdproduto() == 0){
            return ResponseEntity.badRequest().build();
        }

        if(obj.getProduto() == null){
            produto = produtoService.findById(obj.getId().getIdproduto()).getBody();
            obj.setProduto(produto);
        }
        if(obj.getCompra() == null){
            compra = compraService.findById(obj.getId().getIdcompra()).getBody();
            obj.setCompra(compra);
        }

        // Verifica se a quantidade está definida
        if(obj.getQntd() == null){
            if (obj.getProduto().isUnitario()){
                obj.setQntd(1.0);
            } else {
                double peso = balancaService.getUltimoPeso();
                obj.setQntd(peso);
            }
        }
        //Verifica se o preço e o custo estão definidos 
        if(obj.getPreco() == null){
            obj.setPreco(obj.getProduto().getPreco());
        }
        if(obj.getCusto() == null){
            obj.setCusto(obj.getProduto().getCusto());
        }

        Cliente cliente = obj.getCompra().getCliente();
        double saldoAtualizado = cliente.getSaldo() + cliente.getLimite() - obj.getCompra().getTotal() - (obj.getPreco() * obj.getQntd());

        if(saldoAtualizado < 0){
            throw new RuntimeException("Saldo insuficiente.");
        }

        try {
            ResponseEntity<CompraProduto> cp = ((CompraProdutoService) genericService).findByCompraProdutoKey(obj.getId());
            obj.setQntd(cp.getBody().getQntd() + obj.getQntd());
            return edit(cp.getBody().getId(), obj);
        } catch (Exception e) {
            return super.add(obj);
        }
    }

    @DeleteMapping("/del")
    public ResponseEntity<String> removeWithBody(@Valid @RequestBody CompraProdutoKey id) {
        return ((CompraProdutoService) genericService).remove(id);
    }
}