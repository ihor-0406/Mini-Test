package com.example.authapi.controller;

import com.example.authapi.dto.AuthDto;
import com.example.authapi.model.User;
import com.example.authapi.repository.UserRepository;
import com.example.authapi.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private  final UserRepository users;
    private final PasswordEncoder enc;
    private final JwtService jwt;

    public AuthController(UserRepository users, PasswordEncoder enc, JwtService jwt) {
        this.users = users;
        this.enc = enc;
        this.jwt = jwt;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody AuthDto.RegisterRequest req) {
        if(users.existsByEmail(req.email()))
            return  ResponseEntity.status(409).body("Email already in use");
        users.save(new User(req.email(), enc.encode(req.password())));
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDto.JwtResponse> loginUser(@Valid @RequestBody AuthDto.LoginRequest req) {
        var user = users.findByEmail(req.email()).orElse(null);
        if(user == null || !enc.matches(req.password(), user.getPasswordHash()))
            return  ResponseEntity.status(401).build();
        return ResponseEntity.ok(new AuthDto.JwtResponse(jwt.generateToken(user.getEmail())));
    }
}
