package com.jga.les.controller;

import com.jga.les.model.HistoricoProdutos;
import com.jga.les.model.Produto;
import com.jga.les.service.GenericService;
import com.jga.les.service.HistoricoProdutosService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/historicoprodutos")
public class HistoricoProutosContoller extends GenericController<HistoricoProdutos> {

    public HistoricoProutosContoller(GenericService<HistoricoProdutos> genericApplication) {
        super("/historico",genericApplication);
    }

    @GetMapping("/list/{id}")
    @PreAuthorize("hasAuthority(#root.this.getPermissao(''))")
    public ResponseEntity<List<Produto>> list(@PathVariable Long id) {
        return ((HistoricoProdutosService) genericService).findByProduto(id);
    }
}