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
import java.util.UUID;

@Controller
@RequestMapping("/productos")
@SessionAttributes("productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String showForm(Model model, Principal principal, HttpSession session){
        User usuarioActual = usuarioService.findByUsername(principal.getName());
        List<Producto> productos = productoService.obtenerProductosPorUsuario(usuarioActual);
        model.addAttribute("productos", productos);
        if (session.getAttribute("productos") == null) {
            session.setAttribute("productos", new ArrayList<Producto>());
        }

        model.addAttribute("productos", session.getAttribute("productos"));
        return "ingresar";
    }



    @PostMapping("/agregarProductoTemporal")
    public String agregarProductoTemporal(@RequestParam String nombre,
                                          @RequestParam int stock,
                                          @RequestParam double precio,
                                          Principal principal,
                                          HttpSession session) {
        User usuarioActual = usuarioService.findByUsername(principal.getName());
        List<Producto> productos = (List<Producto>) session.getAttribute("productos");
        if (productos == null){
            productos = new ArrayList<>();
            session.setAttribute("productos", productos);
        }
        Producto producto = new Producto();
        producto.setId(UUID.randomUUID().toString());
        producto.setNombre(nombre);
        producto.setStock(stock);
        producto.setPrecio(precio);
        producto.setUser(usuarioActual);
        productos.add(producto);

        return "redirect:/productos";
    }

    @PostMapping("/guardarProductos")
    public String guardarProductos(HttpSession session, Principal principal) {
        List<Producto> productos = (List<Producto>) session.getAttribute("productos");

        for (Producto producto : productos) {
            productoService.saveProducts(producto, producto.getUser());
        }

        return "redirect:/productos";
    }

    @PostMapping("/eliminarProducto/{id}")
    public String eliminarProducto(@PathVariable String id, HttpSession session, Principal principal) {
        List<Producto> productos = (List<Producto>) session.getAttribute("productos");
        if (productos != null) {
            productos.removeIf(producto -> producto.getId().equals(id));  // Eliminar el producto con el ID correspondiente
        }
        productoService.deleteProduct(id);

        return "redirect:/productos";
    }



}
