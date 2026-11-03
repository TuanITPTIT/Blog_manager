package com.example.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestControllerAdvice // danh dau class chuyen xu ly exception cho cac controller
public class GlobalExceptionHandler {
    // 400 : xu ly loi theo tung truong( user rong or email k hop le)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex){
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 400);
        response.put("message", "Du lieu k hop le");
        response.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    // xu ly loi 404 va 409 (404 : k tim thay user, 409 : du lieu trung)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(ResponseStatusException ex){
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Status", ex.getStatusCode().value());
        response.put("message", ex.getStatusCode());
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }
    // xu ly loi 500 (loi ngoai du kien)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex){
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 500);
        response.put("message", "Internal server error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
