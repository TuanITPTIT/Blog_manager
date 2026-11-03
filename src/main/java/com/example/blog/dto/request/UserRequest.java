package com.example.blog.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
    @NotBlank(message="Username k dc de trong")
    @Size(min = 3 , max = 50)
    String username,

    @NotBlank(message = "email k dc de trong")
    @Email(message = "mail k dung ding dang")
    String email,

    @NotBlank(message = "Ho ten k dc trong")
    String fullName
){
}
