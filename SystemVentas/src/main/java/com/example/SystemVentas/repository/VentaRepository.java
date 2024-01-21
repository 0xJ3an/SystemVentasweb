package com.example.SystemVentas.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.SystemVentas.model.Venta;
public interface VentaRepository extends MongoRepository<Venta, String> {

}
