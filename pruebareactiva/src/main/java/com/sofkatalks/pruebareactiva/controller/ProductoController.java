package com.sofkatalks.pruebareactiva.controller;

import com.sofkatalks.pruebareactiva.model.Producto;
import com.sofkatalks.pruebareactiva.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<Producto>>> consultarProductos() {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll()));
    }

    @PostMapping
    public Mono<ResponseEntity<Producto>> cerarProducto(@RequestBody Producto producto) {
        return service.save(producto).map(producto1 -> ResponseEntity.created(URI.create("/api/productos/".concat(producto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(producto1));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Producto>> consultarProducto(@PathVariable String id) {
        return service.findById(id).map(p -> ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(p)).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Producto>> actualizarProducto(@RequestBody Producto producto, @PathVariable String id) {
        return service.findById(id).flatMap(p -> {
            p.setNombre(producto.getNombre());
            p.setPrecio(producto.getPrecio());
            p.setCategoria(producto.getCategoria());
            return service.update(p);
        }).map(p -> ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(p)).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public  Mono<ResponseEntity<Void>> eliminar(@PathVariable String id){
        return service.findById(id).flatMap(p ->{
            return service.delete(p).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }
}
