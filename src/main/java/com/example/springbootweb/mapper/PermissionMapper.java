package com.example.springbootweb.mapper;

import com.example.springbootweb.dto.request.PermissionRequest;
import com.example.springbootweb.dto.respone.PermissionResponse;
import com.example.springbootweb.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
