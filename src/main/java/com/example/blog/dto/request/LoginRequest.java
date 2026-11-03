package com.example.blog.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest (
    @NotBlank(message = "username k dc bo trong")
    String username,
    @NotBlank(message = "password k dc bo trong")
    String password
){}
