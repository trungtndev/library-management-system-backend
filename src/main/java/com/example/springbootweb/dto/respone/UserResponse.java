package com.example.springbootweb.dto.respone;

import com.example.springbootweb.entity.Role;
import com.example.springbootweb.enums.UserRole;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String password;
    String fullName;
    LocalDate dateOfBirth;
    String email;
    Set<Role> roles;
    LocalDate createdAt;
}
