package com.example.blog.service.impl;


import com.example.blog.dto.request.UserRequest;
import com.example.blog.dto.response.UserResponse;
import com.example.blog.entity.User;
import com.example.blog.repository.UserRepository;
import com.example.blog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    private UserResponse toReponse(User user){
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName()
        );
    }
    private User findUser(Long id){
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id){
        return toReponse(findUser(id));
    }

    @Override
    @Transactional
    public UserResponse createUser(UserRequest request){
        if(userRepository.existsByUsername(request.username())||userRepository.existsByEmail(request.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username or Email already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setFullName(request.fullName());

        User savedUser = userRepository.save(user);
        return toReponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse>getAllUsers(){
        return userRepository.findAll().stream().map(this::toReponse).toList();
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserRequest request){
        User user = findUser(id);
        if(!user.getUsername().equals(request.username()) && userRepository.existsByUsername(request.username())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Username already exists");
        }

        if(!user.getEmail().equals(request.email()) && userRepository.existsByEmail(request.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setFullName(request.fullName());

        User updatedUser = userRepository.save(user);
        return toReponse(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id){
        User user = findUser(id);
        userRepository.delete(user);
    }
}
