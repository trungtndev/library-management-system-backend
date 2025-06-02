package com.example.springbootweb.controller;


import com.example.springbootweb.dto.request.UserCreationRequest;
import com.example.springbootweb.dto.request.UserUpdateRequest;
import com.example.springbootweb.dto.respone.ApiResponse;
import com.example.springbootweb.dto.respone.PaginationResponse;
import com.example.springbootweb.dto.respone.UserDetailResponse;
import com.example.springbootweb.dto.respone.UserSummaryResponse;
import com.example.springbootweb.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
public class UserController {

    UserService userService;

    @PostMapping
    public ApiResponse<UserDetailResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        UserDetailResponse response = userService.createUser(request);

        return ApiResponse.<UserDetailResponse>builder()
                .code(200)
                .success(true)
                .result(response)
                .build();
    }

    @GetMapping
    public ApiResponse<PaginationResponse<UserSummaryResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "fullName") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        PaginationResponse<UserSummaryResponse> response = userService.getAllUsers(pageable);
        return ApiResponse.<PaginationResponse<UserSummaryResponse>>builder()
                .code(200)
                .success(true)
                .result(response)
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<UserDetailResponse> getInfo() {
        UserDetailResponse response = userService.getInfo();
        return ApiResponse.<UserDetailResponse>builder()
                .code(200)
                .result(response)
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserDetailResponse> getUserById(@PathVariable String userId) {
        UserDetailResponse response = userService.getUserById(userId);
        return ApiResponse.<UserDetailResponse>builder()
                .code(200)
                .success(true)
                .result(response)
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserDetailResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        UserDetailResponse response = userService.updateUser(userId, request);
        return ApiResponse.<UserDetailResponse>builder()
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
                .build();
    }
}
