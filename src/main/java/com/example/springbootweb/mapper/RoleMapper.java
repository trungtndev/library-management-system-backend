package com.example.springbootweb.mapper;

import com.example.springbootweb.dto.request.RoleRequest;
import com.example.springbootweb.dto.respone.RoleResponse;
import com.example.springbootweb.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
