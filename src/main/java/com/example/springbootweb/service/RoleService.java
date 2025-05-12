package com.example.springbootweb.service;

import com.example.springbootweb.dto.request.RoleRequest;
import com.example.springbootweb.dto.respone.RoleResponse;
import com.example.springbootweb.entity.Permission;
import com.example.springbootweb.entity.Role;
import com.example.springbootweb.mapper.RoleMapper;
import com.example.springbootweb.repository.PermissionRepository;
import com.example.springbootweb.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        Role role = roleMapper.toRole(request);

        List<Permission> permissions = permissionRepository.findAllById(request.getPermission());
        role.setPermission(new HashSet<>(permissions));

        role = roleRepository.save(role);

        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        List<Role> roles = roleRepository.findAll();

        return roles.stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public void delete(String roleName) {
        roleRepository.deleteById(roleName);
    }

}
