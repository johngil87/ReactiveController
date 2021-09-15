package com.sofkatalks.pruebareactiva.repository;

import com.sofkatalks.pruebareactiva.model.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductoRepository extends ReactiveMongoRepository<Producto, String> {
}
