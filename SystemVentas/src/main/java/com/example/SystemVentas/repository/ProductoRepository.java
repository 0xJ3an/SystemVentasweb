package com.example.SystemVentas.repository;

import com.example.SystemVentas.model.Producto;
import com.example.SystemVentas.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductoRepository extends MongoRepository<Producto, String> {
    List<Producto> findByUser (User usuario);
}
