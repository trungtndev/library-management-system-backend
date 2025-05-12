package com.example.springbootweb.service;

import com.example.springbootweb.dto.request.UserCreationRequest;
import com.example.springbootweb.dto.request.UserUpdateRequest;
import com.example.springbootweb.dto.respone.UserResponse;
import com.example.springbootweb.entity.Role;
import com.example.springbootweb.entity.User;
import com.example.springbootweb.enums.UserRole;
import com.example.springbootweb.exception.AppException;
import com.example.springbootweb.exception.ErrorCode;
import com.example.springbootweb.mapper.UserMapper;
import com.example.springbootweb.repository.RoleRepository;
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

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest userRequest) {

        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(userRequest);


        // add role
        Role memberRole = roleRepository.findById(UserRole.MEMBER.name())
                .orElseThrow(
                        () -> new AppException(ErrorCode.ROLE_NOT_FOUND)
                );
        Set<Role> roles = new HashSet<>();
        roles.add(memberRole);

        user.setRole(roles);
        user.setCreatedAt(LocalDate.now());

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public List<UserResponse> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'MEMBER')")
    public UserResponse getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new AppException(ErrorCode.USER_NOT_FOUND)
                );
        return userMapper.toUserResponse(user);
    }

    public UserResponse getInfo() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new AppException(ErrorCode.USER_NOT_FOUND)
                );

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasAnyRole('MEMBER', 'ADMIN', 'LIBRARIAN')")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new AppException(ErrorCode.USER_NOT_FOUND)
                );

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRole(new HashSet<>(user.getRole()));

        return userMapper.toUserResponse(userRepository.save(user));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        userRepository.delete(user);
    }

}
