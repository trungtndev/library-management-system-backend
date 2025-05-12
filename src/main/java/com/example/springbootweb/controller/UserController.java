package com.example.springbootweb.controller;


import com.example.springbootweb.dto.request.UserCreationRequest;
import com.example.springbootweb.dto.request.UserUpdateRequest;
import com.example.springbootweb.dto.respone.ApiResponse;
import com.example.springbootweb.dto.respone.UserResponse;
import com.example.springbootweb.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/user")
public class UserController {

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
        List<UserResponse> response = userService.getAllUsers();
        return ApiResponse.<List<UserResponse>>builder()
                .code(200)
                .success(true)
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
                .success(true)
                .result(response)
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.updateUser(userId, request);
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .success(true)
                .result(response)
                .build();
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);


        return ApiResponse.<String>builder()
                .code(200)
                .success(true)
                .result("User deleted successfully")
                .build();
    }
}
