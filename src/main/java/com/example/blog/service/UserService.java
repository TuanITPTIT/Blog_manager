package com.example.blog.service;

import com.example.blog.dto.request.UserRequest;
import com.example.blog.dto.response.UserResponse;
import java.util.*;

public interface UserService {
    UserResponse createUser(UserRequest request);
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    UserResponse updateUser(Long id, UserRequest request);
    void deleteUser(Long id);

}
