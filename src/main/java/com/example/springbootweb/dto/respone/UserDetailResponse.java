package com.example.springbootweb.dto.respone;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailResponse {
    String id;
    String username;
    String fullName;
    LocalDate dateOfBirth;
    String email;
    LocalDate createdAt;
}
