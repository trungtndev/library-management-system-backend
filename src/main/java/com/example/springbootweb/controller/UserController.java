package com.example.springbootweb.controller;


import com.example.springbootweb.dto.request.UserCreationRequest;
import com.example.springbootweb.dto.request.UserUpdateRequest;
import com.example.springbootweb.dto.respone.ApiResponse;
import com.example.springbootweb.dto.respone.UserResponse;
import com.example.springbootweb.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        UserResponse response = userService.createUser(request);

        return ApiResponse.<UserResponse>builder()
                .code(200)
                .success(true)
                .result(response)
                .build();
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {

        var a = SecurityContextHolder .getContext().getAuthentication();

        log.info("User: {}", a.getName());
        a.getAuthorities().forEach(
                role -> {log.info(role.getAuthority());}
        );
        List<UserResponse> response = userService.getAllUsers();

        return ApiResponse.<List<UserResponse>>builder()
                .code(200)
                .result(response)
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getInfo() {
        UserResponse response = userService.getInfo();
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .result(response)
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUserById(@PathVariable String userId) {
        UserResponse response = userService.getUserById(userId);
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .result(response)
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.updateUser(userId, request);
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .result(response)
                .build();
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<String> deleteUser(@PathVariable String userId) {
        try {
            userService.deleteUser(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage());
        }

        return ApiResponse.<String>builder()
                .code(200)
                .result("User deleted successfully")
                .build();
    }
}
