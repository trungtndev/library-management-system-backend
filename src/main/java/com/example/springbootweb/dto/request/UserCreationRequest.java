package com.example.springbootweb.dto.request;

import com.example.springbootweb.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 5, message = "USERNAME_INVALID_LENGTH")
    String username;

    @Size(min = 8, message = "PASSWORD_INVALID_LENGTH")
    String password;

    @DobConstraint(min = 10, message = "INVALID_DATE_OF_BIRTH")
    LocalDate dateOfBirth;
    String fullName;
    String email;
}