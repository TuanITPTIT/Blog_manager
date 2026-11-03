package com.example.blog.dto.response;

public record LoginResponse (
    String message,
    Long userId,
    String username
) {
}
