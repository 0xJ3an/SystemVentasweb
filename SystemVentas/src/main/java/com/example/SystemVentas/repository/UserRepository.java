package com.example.SystemVentas.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.SystemVentas.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}
