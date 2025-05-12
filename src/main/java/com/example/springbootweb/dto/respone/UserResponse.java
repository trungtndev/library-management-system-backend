package com.example.springbootweb.dto.respone;

import com.example.springbootweb.entity.Role;
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
    String fullName;
    LocalDate dateOfBirth;
    String email;
    Set<Role> role;
    LocalDate createdAt;
}
