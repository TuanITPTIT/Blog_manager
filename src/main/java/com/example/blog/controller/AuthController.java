package com.example.blog.controller;

import com.example.blog.dto.request.LoginRequest;
import com.example.blog.dto.request.RegisterRequest;
import com.example.blog.dto.response.LoginResponse;
import com.example.blog.dto.response.UserResponse;
import com.example.blog.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse>login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }
    @GetMapping("/me")
    public UserResponse getCurrentUser(Authentication authentication){
        String username = authentication.getName();
        return authService.getCurrentUser(username);
    }
}
