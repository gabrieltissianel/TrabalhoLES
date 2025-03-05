package com.jga.les.controller;

import java.util.List;

import com.jga.les.service.GenericService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GenericController<E> {

    protected final GenericService<E> genericApplication;

    @GetMapping("/list")
    public ResponseEntity<List<E>> list() {
        return genericApplication.list();
    }

    @PostMapping("/add")
    public ResponseEntity<E> add(@Valid @RequestBody E obj) {
        return genericApplication.add(obj);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(@PathVariable long id) {
        return genericApplication.remove(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<E> edit(@PathVariable long id, @Valid @RequestBody E obj) {
        return genericApplication.update(obj, id);
    }

}