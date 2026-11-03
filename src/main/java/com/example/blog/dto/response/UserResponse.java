package com.example.blog.dto.response;

public record UserResponse (
        Long id,
        String username,
        String email,
        String fullName
){
}
