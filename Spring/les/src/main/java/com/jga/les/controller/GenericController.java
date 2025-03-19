package com.jga.les.controller;

import java.util.List;

import com.jga.les.service.GenericService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GenericController<E> {

    protected final GenericService<E> genericService;

    @GetMapping("/list")
    public ResponseEntity<List<E>> list() {
        return genericService.list();
    }

    @PostMapping("/add")
    public ResponseEntity<E> add(@Valid @RequestBody E obj) {
        return genericService.add(obj);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(@PathVariable long id) {
        return genericService.remove(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<E> edit(@PathVariable long id, @Valid @RequestBody E obj) {
        return genericService.update(obj, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<E> findById(@PathVariable long id) {
        return genericService.findById(id);
    }
}