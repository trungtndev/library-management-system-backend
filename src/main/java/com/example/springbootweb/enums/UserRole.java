package com.example.springbootweb.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum UserRole {
    ADMIN("ADMIN"),
    LIBRARIAN("LIBRARIAN"),
    MEMBER("MEMBER");

    String role;
}
