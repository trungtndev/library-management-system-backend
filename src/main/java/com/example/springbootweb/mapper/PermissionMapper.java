package com.example.springbootweb.mapper;

import com.example.springbootweb.dto.request.PermissionRequest;
import com.example.springbootweb.dto.request.UserCreationRequest;
import com.example.springbootweb.dto.respone.PermissionResponse;
import com.example.springbootweb.dto.respone.UserResponse;
import com.example.springbootweb.entity.Permission;
import com.example.springbootweb.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
