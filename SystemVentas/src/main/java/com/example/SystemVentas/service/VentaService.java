package com.example.SystemVentas.service;

import com.example.SystemVentas.model.DetalleVenta;
import com.example.SystemVentas.model.User;
import com.example.SystemVentas.model.Venta;
import com.example.SystemVentas.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.SystemVentas.repository.UserRepository;

import java.security.Principal;
import java.util.List;

@Service
public class VentaService {
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UserRepository usuarioRepository;

    public void realizarVenta(List<DetalleVenta> detalles, User usuario){
        Venta venta = new Venta();
        venta.setDetalles(detalles);
        venta.setUsuario(usuario);
        venta.setTotal(detalles.stream().mapToDouble(DetalleVenta::getPrecioTotal).sum()); // Calcular el total de la venta

        for (DetalleVenta detalle : detalles) {
            productoService.disminuirStock(detalle.getProducto().getId(), detalle.getCantidad()); // Disminuir el stock del producto vendido
        }

        if (usuarioRepository.findByUsername(usuario.getName()) != null) {
            usuario.setNumVentas(usuario.getNumVentas() + 1);
            usuarioRepository.save(usuario); // Guardar el usuario actualizado
        }

        usuario.incrementarVentasRealizadas();
        ventaRepository.save(venta);
    }

    private double calcularTotal(List<DetalleVenta> detalles){
        double total = 0;
        for(DetalleVenta detalle : detalles){
            total += detalle.getPrecioTotal();
        }
        return total;
    }
}
