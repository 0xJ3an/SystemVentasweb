package com.example.SystemVentas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.SystemVentas.repository.UserRepository;
import com.example.SystemVentas.model.*;

@Service
public class UsuarioService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
       
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setNumVentas(0);
       

        return userRepository.save(user);
    }

    public void saveUser(User user){
        userRepository.save(user);
    } 

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public void incrementarVentas(User user){
        user.setNumVentas(user.getNumVentas()+1);
        userRepository.save(user);
    }

}
