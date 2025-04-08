package com.example.springbootweb.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED(999, "Uncategorized error"),

    USER_NOT_FOUND(100, "User not found"),
    USER_NOT_EXISTS(401, "User does not exist"),
    USER_ALREADY_EXISTS(402, "User already exists"),
    USERNAME_INVALID_LENGTH(403, "Username must be at least 5 characters"),
    PASSWORD_INVALID_LENGTH(404, "Password must be at least 8 characters"),

    UNAUTHORIZED(410, "Unauthorized"),

    INTERNAL_SERVER_ERROR(500, "Internal server error");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
