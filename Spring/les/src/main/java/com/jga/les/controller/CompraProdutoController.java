package com.jga.les.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jga.les.model.CompraProduto;
import com.jga.les.model.CompraProdutoKey;
import com.jga.les.service.CompraProdutoService;
import com.jga.les.service.GenericService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/compraproduto")
public class CompraProdutoController extends GenericController<CompraProduto, CompraProdutoKey> {

    public CompraProdutoController(GenericService<CompraProduto, CompraProdutoKey> genericApplication) {
        super("/compraproduto", genericApplication);
    }

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<CompraProduto> add(@Valid CompraProduto obj) {
        System.out.println("--------------------\n");
        System.out.println(obj);
        System.out.println("--------------------\n");
        try {
            ResponseEntity<CompraProduto> cp = ((CompraProdutoService) genericService).findByCompraProdutoKey(obj.getId());
            obj.setQntd(cp.getBody().getQntd() + obj.getQntd());

            return edit(cp.getBody().getId(), obj);
        } catch (Exception e) {
            return super.add(obj);
        }
    }
}