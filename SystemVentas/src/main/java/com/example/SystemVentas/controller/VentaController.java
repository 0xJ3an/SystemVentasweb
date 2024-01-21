package com.example.SystemVentas.controller;

import com.example.SystemVentas.model.DetalleVenta;
import com.example.SystemVentas.model.Producto;
import com.example.SystemVentas.model.User;
import com.example.SystemVentas.service.ProductoService;
import com.example.SystemVentas.service.UsuarioService;
import com.example.SystemVentas.service.VentaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaController {
    @Autowired
    private ProductoService productoService;

    @Autowired
    private VentaService ventaService;

    @Autowired
    private UsuarioService usuarioService;

    // Mostrar página de venta de productos
    @GetMapping
    public String mostrarVenderProductos(HttpSession session, Model model, Principal principal) {
        List<DetalleVenta> detallesVenta = (List<DetalleVenta>) session.getAttribute("detallesVenta");
        if (detallesVenta == null) {
            detallesVenta = new ArrayList<>();
            session.setAttribute("detallesVenta", detallesVenta);
        }
        final double IVA = 0.12;
        model.addAttribute("IVA", IVA);
        model.addAttribute("detallesVenta", detallesVenta);
        model.addAttribute("productos", productoService.listarProductos());
        return "Venta"; // Nombre de la vista HTML para "Vender Productos"
    }

    // Añadir producto a la venta temporalmente
    @PostMapping("/añadirProductoVenta")
    public String añadirProductoVenta(@RequestParam String productoId, @RequestParam int cantidad, HttpSession session, Model model) {
        List<DetalleVenta> detallesVenta = (List<DetalleVenta>) session.getAttribute("detallesVenta");
        if (detallesVenta == null) {
            detallesVenta = new ArrayList<>();
        }
        Producto producto = productoService.obtenerProductoPorId(productoId);
        if (producto != null && cantidad <= producto.getStock()) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioTotal(detalle.getPrecioConIva() * cantidad); // Calcular precio total para el detalle
            detallesVenta.add(detalle);
        }
        session.setAttribute("detallesVenta", detallesVenta);
        model.addAttribute("detallesVenta", detallesVenta); // Añadir detalles de venta al modelo para mostrar en la vista
        model.addAttribute("productos", productoService.listarProductos()); // Añadir productos al modelo para el formulario de selección

        return "redirect:/ventas";
    }

    // Finalizar la venta
    @PostMapping("/finalizarVenta")
    public String finalizarVenta(HttpSession session, RedirectAttributes redirectAttributes, Principal principal) {
        List<DetalleVenta> detallesVenta = (List<DetalleVenta>) session.getAttribute("detallesVenta");
        if (detallesVenta != null && !detallesVenta.isEmpty()) {
            User usuarioActual = usuarioService.findByUsername(principal.getName());
            ventaService.realizarVenta(detallesVenta, usuarioActual); // Realizar la venta
            session.removeAttribute("detallesVenta"); // Limpiar la lista de detalles de venta en la sesión
            redirectAttributes.addFlashAttribute("mensaje", "Venta realizada con éxito.");
        }
        return "redirect:/home"; // Redirigir al home con mensaje de éxito
    }

    @GetMapping("/calcularTotal")
    public String calcularTotal(HttpSession session, Model model) {
        List<DetalleVenta> detallesVenta = (List<DetalleVenta>) session.getAttribute("detallesVenta");
        double total = 0;
        if (detallesVenta != null) {
            total = detallesVenta.stream().mapToDouble(DetalleVenta::getPrecioTotal).sum();
        }

        model.addAttribute("detallesVenta", detallesVenta); // Pasar los detalles actualizados a la vista
        model.addAttribute("total", total); // Pasar el total calculado a la vista
        model.addAttribute("productos", productoService.listarProductos()); // Añadir productos al modelo para el formulario de selección

        return "Venta"; // Nombre de la vista HTML para "Vender Productos"
    }

    @PostMapping("/eliminarProductoVenta/{indiceProducto}")
    public String eliminarProductoVenta(@PathVariable String indiceProducto, HttpSession session) {
        List<DetalleVenta> detallesVenta = (List<DetalleVenta>) session.getAttribute("detallesVenta");
        if (detallesVenta != null) {
            detallesVenta.removeIf(detalleVenta -> detalleVenta.getProducto().getId().equals(indiceProducto));

        }
        return "redirect:/ventas"; // Redirigir a la misma página para mostrar la lista actualizada
    }
}
