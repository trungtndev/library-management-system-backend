package com.example.springbootweb.service;

import com.example.springbootweb.dto.request.PermissionRequest;
import com.example.springbootweb.dto.respone.PermissionResponse;
import com.example.springbootweb.entity.Permission;
import com.example.springbootweb.mapper.PermissionMapper;
import com.example.springbootweb.repository.PermissionRepository;
import com.example.springbootweb.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    RoleRepository roleRepository;
    PermissionMapper permissionMapper;

    @PreAuthorize("hasAnyRole('ADMIN')")
    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);

        permission = permissionRepository.save(permission);

        return permissionMapper.toPermissionResponse(permission);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public void delete(String permissionName) {
        permissionRepository.deleteById(permissionName);
    }

//    @PreAuthorize("hasAnyRole('ADMIN')")
//    public List<PermissionResponse> getPermissionsByRoleName(String roleName){
//        Role role = roleRepository.findById(roleName)
//                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
//        Set<Role> roles = new HashSet<>();
//        roles.add(role);
//        Optional<Permission> permissions = permissionRepository.findByRole(roles);
//
//        return permissions.stream()
//                .map(permissionMapper::toPermissionResponse)
//                .collect(Collectors.toList());
//    }
}
