package com.example.blog.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest (

    @NotBlank(message ="Username k dc de trong")
    @Size(min=3, max = 50)
    String username,

    @NotBlank(message = "email k dc trong")
    @Email(message = "email k hop le")
    String email,

    @NotBlank(message = "pass k dc de trong")
    @Size(min = 8, message = "pass > 8 ki tu")
    String password,

    @NotBlank(message = "fullname k dc trong")
    String fullName
){}
