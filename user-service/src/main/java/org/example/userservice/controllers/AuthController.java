package org.example.userservice.controllers;


import org.example.userservice.models.User;
import org.example.userservice.repositories.UserRepository;
import org.example.userservice.services.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try{
            if (userRepository.existById(user.getUid())) {
                return ResponseEntity.badRequest().body("User already exists");
            }

            if (userRepository.getUserBy("username", user.getUsername()) != null){
                return ResponseEntity.badRequest().body("Username already exists");
            }

            userRepository.saveUser(user);
            return ResponseEntity.ok("User created");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
