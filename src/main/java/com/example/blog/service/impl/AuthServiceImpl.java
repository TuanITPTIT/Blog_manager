package com.example.blog.service.impl;

import com.example.blog.dto.request.LoginRequest;
import com.example.blog.dto.request.RegisterRequest;
import com.example.blog.dto.response.LoginResponse;
import com.example.blog.dto.response.UserResponse;
import com.example.blog.entity.Role;
import com.example.blog.entity.User;
import com.example.blog.repository.UserRepository;
import com.example.blog.security.JwtService;
import com.example.blog.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final long expirationMinutes;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            @Value("${app.jwt.expiration-minutes}")
            long expirationMinutes
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.expirationMinutes = expirationMinutes;
    }
    @Override
    @Transactional
    public UserResponse register(RegisterRequest request){
        if(userRepository.existsByUsername(request.username())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Username already exists");
        }
        if(userRepository.existsByEmail(request.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setFullName(request.fullName());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser.getId(),savedUser.getUsername(), savedUser.getEmail(),savedUser.getFullName());
    }

    @Override
    public LoginResponse login(LoginRequest request){
        try{
            var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
            User user = userRepository.findByUsername(authentication.getName()).orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid credentials"));
            if(user.getRole() == null){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Account role is not assigned");
            }
            String token = jwtService.generateToken(user);
            return new LoginResponse("Login success", user.getId(),user.getUsername(), token, "Bearer", expirationMinutes * 60);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid username or password");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(String username){
        User user = userRepository.findByUsername(username).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getFullName());
    }

}
