package com.example.springbootweb.controller;


import com.example.springbootweb.dto.request.RoleRequest;
import com.example.springbootweb.dto.respone.ApiResponse;
import com.example.springbootweb.dto.respone.RoleResponse;
import com.example.springbootweb.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        RoleResponse response = roleService.create(request);

        return ApiResponse.<RoleResponse>builder()
                .result(response)
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        List<RoleResponse> responses = roleService.getAll();
        return ApiResponse.<List<RoleResponse>>builder()
                .result(responses)
                .build();
    }

    @GetMapping("/{roleName}")
    ApiResponse<RoleResponse> getRole(@PathVariable String roleName) {
        RoleResponse response = roleService.getRole(roleName);
        return ApiResponse.<RoleResponse>builder()
                .result(response)
                .build();
    }

    @DeleteMapping("/{roleName}")
    ApiResponse<Void> deleteRole(@PathVariable String roleName) {
        roleService.delete(roleName);
        return ApiResponse.<Void>builder()
                .build();
    }
}
