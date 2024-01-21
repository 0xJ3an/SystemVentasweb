package com.example.SystemVentas.controller;

import com.example.SystemVentas.model.DetalleVenta;
import com.example.SystemVentas.model.Producto;
import com.example.SystemVentas.model.User;
import com.example.SystemVentas.model.Venta;
import com.example.SystemVentas.repository.UserRepository;
import com.example.SystemVentas.service.AuthenticationService;
import com.example.SystemVentas.service.ProductoService;
import com.example.SystemVentas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UsuarioService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {

        return "login";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        User registeredUser = userService.registerUser(user);

        // Agregar el número de cuenta al modelo

        model.addAttribute("registrationSuccess", true);

        return "register";
    }

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping ("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model) {
        if (authenticationService.authenticate(username, password)) {
            return "home";  // Redirige al dashboard si la autenticación es exitosa
        } else {
            model.addAttribute("loginError", true);
            return "login";  // Vuelve a mostrar la página de login con un mensaje de error
        }
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal){
        String username = principal.getName();
        User user = userService.findByUsername(username);
        // Agregar el número de cuenta al modelo
        model.addAttribute("user", user.getName());
        model.addAttribute("numVentas", user.getNumVentas() );

        return "home";
    }
    @Autowired
    private ProductoService productoService;
    @GetMapping("/ingresar")
    public String ingresarProdcutos( Model model){
        return "ingresar";
    }
    @GetMapping("/venderProductos")
    public String showVenderProductos(Model model){
        List<Producto> productosDisponibles = productoService.listarProductos();
        model.addAttribute("productos", productosDisponibles);
        return "Venta";
    }

}
