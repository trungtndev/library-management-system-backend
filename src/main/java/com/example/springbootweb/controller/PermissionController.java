package com.example.springbootweb.controller;


import com.example.springbootweb.dto.request.PermissionRequest;
import com.example.springbootweb.dto.respone.ApiResponse;
import com.example.springbootweb.dto.respone.PermissionResponse;
import com.example.springbootweb.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        PermissionResponse response = permissionService.create(request);

        return ApiResponse.<PermissionResponse>builder()
                .result(response)
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {
        List<PermissionResponse> response = permissionService.getAll();
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(response)
                .build();
    }
//    @GetMapping("/{roleName}")
//    ApiResponse<List<PermissionResponse>> getPermissionsByRoleName(@PathVariable String roleName){
//        List<PermissionResponse> response = permissionService.getPermissionsByRoleName(roleName);
//        return ApiResponse.<List<PermissionResponse>>builder()
//                .result(response)
//                .build();
//    }

    @DeleteMapping("/{permissionName}")
    ApiResponse<Void> delete(@PathVariable String permissionName) {
        permissionService.delete(permissionName);
        return ApiResponse.<Void>builder()
                .build();
    }
}
