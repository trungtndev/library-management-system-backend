package com.example.springbootweb.service;

import com.example.springbootweb.dto.request.PermissionRequest;
import com.example.springbootweb.dto.request.UserCreationRequest;
import com.example.springbootweb.dto.respone.PermissionResponse;
import com.example.springbootweb.dto.respone.UserResponse;
import com.example.springbootweb.entity.Permission;
import com.example.springbootweb.entity.Role;
import com.example.springbootweb.entity.User;
import com.example.springbootweb.exception.AppException;
import com.example.springbootweb.exception.ErrorCode;
import com.example.springbootweb.mapper.PermissionMapper;
import com.example.springbootweb.mapper.UserMapper;
import com.example.springbootweb.repository.PermissionRepository;
import com.example.springbootweb.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);

        permission = permissionRepository.save(permission);

        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll(){
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permissionName){
        permissionRepository.deleteById(permissionName);
    }

}
