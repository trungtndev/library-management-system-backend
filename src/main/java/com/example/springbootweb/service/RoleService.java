package com.example.springbootweb.service;

import com.example.springbootweb.dto.request.RoleRequest;
import com.example.springbootweb.dto.respone.RoleResponse;
import com.example.springbootweb.entity.Permission;
import com.example.springbootweb.entity.Role;
import com.example.springbootweb.exception.AppException;
import com.example.springbootweb.exception.ErrorCode;
import com.example.springbootweb.mapper.RoleMapper;
import com.example.springbootweb.repository.PermissionRepository;
import com.example.springbootweb.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @PreAuthorize("hasAnyRole('ADMIN')")
    public RoleResponse create(RoleRequest request) {
        Role role = roleMapper.toRole(request);

        List<Permission> permissions = permissionRepository.findAllById(request.getPermission());
        role.setPermission(new HashSet<>(permissions));

        role = roleRepository.save(role);

        return roleMapper.toRoleResponse(role);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<RoleResponse> getAll() {
        List<Role> roles = roleRepository.findAll();

        return roles.stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public void delete(String roleName) {
        roleRepository.deleteById(roleName);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public RoleResponse getRole(String roleName) {
        roleName = roleName.toUpperCase();

        Role role = roleRepository.findById(roleName)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        return roleMapper.toRoleResponse(role);
    }
}
