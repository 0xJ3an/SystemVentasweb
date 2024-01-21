package com.example.SystemVentas.service;

import com.example.SystemVentas.model.Producto;
import com.example.SystemVentas.model.User;
import com.example.SystemVentas.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarProductos(){
        return productoRepository.findAll();
    }
    public void saveProducts(Producto producto, User user){
        producto.setUser(user);
        productoRepository.save(producto);
    }

    public List<Producto> obtenerProductosPorUsuario(User user) {
        return productoRepository.findByUser(user);
    }

    public Producto obtenerProductoPorId(String id) {
        return productoRepository.findById(id).orElse(null);
    }


    public void deleteProduct(String id){
        productoRepository.deleteById(id);
    }

    public void disminuirStock(String productoId, int cantidad) {
        Producto producto = productoRepository.findById(productoId).orElse(null);
        if (producto != null && cantidad <= producto.getStock()) {
            producto.setStock(producto.getStock() - cantidad); // Disminuir el stock
            productoRepository.save(producto); // Guardar el producto actualizado
        } else {
            // Manejar la situación en la que el stock no es suficiente o el producto no se encuentra
        }
    }
}
