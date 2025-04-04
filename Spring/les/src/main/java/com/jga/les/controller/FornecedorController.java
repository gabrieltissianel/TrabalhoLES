package com.jga.les.controller;

import com.jga.les.model.Fornecedor;
import com.jga.les.service.GenericService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fornecedor")
public class FornecedorController extends GenericController<Fornecedor> {

    public FornecedorController(GenericService<Fornecedor> genericApplication) {
        super(genericApplication);
    }


    @Override
    @PreAuthorize("hasAuthority('FORNECEDOR')")
    public ResponseEntity<List<Fornecedor>> list() {
        return super.list();
    }

    @Override
    @PreAuthorize("hasAuthority('FORNECEDOR_ADD')")
    public ResponseEntity<Fornecedor> add(Fornecedor obj) {
        return super.add(obj);
    }

    @Override
    @PreAuthorize("hasAuthority('FORNECEDOR_ADD')")
    public ResponseEntity<String> remove(long id) {
        return super.remove(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FORNECEDOR_EDIT')")
    public ResponseEntity<Fornecedor> edit(long id, Fornecedor obj) {
        return super.edit(id, obj);
    }

    @Override
    @PreAuthorize("hasAuthority('FORNECEDOR')")
    public ResponseEntity<Fornecedor> findById(long id) {
        return super.findById(id);
    }
}