package com.jga.les.controller;

import com.jga.les.service.PagamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jga.les.model.Pagamento;
import com.jga.les.service.GenericService;

import java.util.List;

@RestController
@RequestMapping("/pagamento")
public class PagamentoController extends GenericController<Pagamento>{
    
    public PagamentoController(GenericService<Pagamento> genericApplication){
        super("/pagamento",genericApplication);
    }

    @GetMapping("/list/{id}")
    @PreAuthorize("hasAuthority(#root.this.getPermissao(''))")
    public ResponseEntity<List<Pagamento>> list(@PathVariable Long id) {
        return ((PagamentoService) genericService).findByFornecedor(id);
    }
}
