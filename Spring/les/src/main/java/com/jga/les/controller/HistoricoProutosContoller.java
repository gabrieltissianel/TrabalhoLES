package com.jga.les.controller;

import com.jga.les.model.HistoricoProdutos;
import com.jga.les.service.GenericService;
import com.jga.les.service.HistoricoProdutosService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/historicoprodutos")
public class HistoricoProutosContoller extends GenericController<HistoricoProdutos, Long> {

    public HistoricoProutosContoller(GenericService<HistoricoProdutos, Long> genericApplication) {
        super("/historicoproduto",genericApplication);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<List<HistoricoProdutos>> list(@PathVariable Long id) {
        return ((HistoricoProdutosService) genericService).findByProduto(id);
    }
}