package com.example.springbootweb.configuration;

import com.example.springbootweb.entity.Permission;
import com.example.springbootweb.entity.Role;
import com.example.springbootweb.entity.User;
import com.example.springbootweb.enums.RolePermission;
import com.example.springbootweb.enums.UserRole;
import com.example.springbootweb.repository.PermissionRepository;
import com.example.springbootweb.repository.RoleRepository;
import com.example.springbootweb.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner init(UserRepository userRepository, RoleRepository roleRepository, PermissionRepository permissionRepository) {
        return args -> {

            // Add data to the Permission table
            for (RolePermission p : RolePermission.values()) {
                if (permissionRepository.findById(p.getPermission()).isEmpty()) {
                    Permission permission = Permission.builder()
                            .name(p.getPermission())
                            .build();

                    permissionRepository.save(permission);
                }
            }


            // Add data to the Roles table
            // 1. add Admin role
            if (roleRepository.findById(UserRole.ADMIN.toString()).isEmpty()) {
                EnumSet<RolePermission> rolePermissions = EnumSet.allOf(RolePermission.class);
                Set<Permission> permissions = new HashSet<>(
                        permissionRepository.findAllById(rolePermissions.stream()
                                .map(RolePermission::getPermission)
                                .toList()
                        ));
                Role userRole = Role.builder()
                        .name(UserRole.ADMIN.toString())
                        .permission(permissions)
                        .build();
                roleRepository.save(userRole);
            }
            // 2. add librarian role
            if (roleRepository.findById(UserRole.LIBRARIAN.name()).isEmpty()) {
                Set<Permission> permissions = new HashSet<>(
                        permissionRepository.findAllById(Arrays.asList(
                                RolePermission.MANAGE_BOOKS.getPermission(),
                                RolePermission.MANAGE_USERS.getPermission(),
                                RolePermission.MANAGE_CATEGORIES.getPermission(),
                                RolePermission.MANAGE_REVIEWS.getPermission(),
                                RolePermission.MANAGE_PAYMENTS.getPermission()
                        ))
                );
                Role userRole = Role.builder()
                        .name(UserRole.LIBRARIAN.name())
                        .permission(permissions)
                        .build();
                roleRepository.save(userRole);
            }

            // 3. add member role
            if (roleRepository.findById(UserRole.MEMBER.name()).isEmpty()) {
                Set<Permission> permissions = new HashSet<>(
                        permissionRepository.findAllById(Arrays.asList(
                                RolePermission.BORROW_BOOK.getPermission(),
                                RolePermission.RETURN_BOOK.getPermission(),
                                RolePermission.VIEW_LOAN_HISTORY.getPermission(),
                                RolePermission.WRITE_REVIEW.getPermission(),
                                RolePermission.MAKE_PAYMENT.getPermission()
                        ))
                );
                Role userRole = Role.builder()
                        .name(UserRole.MEMBER.name())
                        .permission(permissions)
                        .build();
                roleRepository.save(userRole);
            }


            // Add data to the User table
            if (userRepository.findByUsername("admin").isEmpty()) {
                Role adminRole = Role.builder()
                        .name(UserRole.ADMIN.toString())
                        .build();

                Set<Role> roles = new HashSet<Role>();
                roles.add(adminRole);

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .role(roles)
                        .build();
                userRepository.save(user);
                log.warn("Admin user created with default password.");
            }
        };
    }
}
