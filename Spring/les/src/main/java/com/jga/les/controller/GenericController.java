package com.jga.les.controller;

import java.util.List;

import com.jga.les.service.GenericService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GenericController<E> {

    protected String nomeTela;

    protected final GenericService<E> genericService;

    public String getNomeTela(String tipo) {
        return nomeTela +tipo;
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority(#root.this.getNomeTela(''))")
    public ResponseEntity<List<E>> list() {
        return genericService.list();
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority(#root.this.getNomeTela('/add'))")
    public ResponseEntity<E> add(@Valid @RequestBody E obj) {
        return genericService.add(obj);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(#root.this.getNomeTela('/delete'))")
    public ResponseEntity<String> remove(@PathVariable long id) {
        return genericService.remove(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(#root.this.getNomeTela('/edit'))")
    public ResponseEntity<E> edit(@PathVariable long id, @Valid @RequestBody E obj) {
        return genericService.update(obj, id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority(#root.this.getNomeTela(''))")
    public ResponseEntity<E> findById(@PathVariable long id) {
        return genericService.findById(id);
    }
}