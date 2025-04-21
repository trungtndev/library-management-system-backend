package com.example.springbootweb.configuration;

import com.example.springbootweb.entity.Role;
import com.example.springbootweb.entity.User;
import com.example.springbootweb.enums.UserRole;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner init(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {

            if (roleRepository.findById(UserRole.USER.toString()).isEmpty()) {
                Role userRole = Role.builder()
                        .name(UserRole.ADMIN.toString())
                        .build();

                roleRepository.save(userRole);
            }


            if (userRepository.findByUsername("admin").isEmpty()){

                Role adminRole = Role.builder()
                        .name(UserRole.ADMIN.toString())
                        .build();

                Set<Role> roles = new HashSet<Role>();
                roles.add(adminRole);

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();
                userRepository.save(user);
                log.warn("Admin user created with default password.");
            }
        };
    }
}
