package com.example.springbootweb.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED(999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    USER_NOT_FOUND(401, "User not found", HttpStatus.NOT_FOUND),
    USER_EXISTED(402, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID_LENGTH(403, "Username must be at least 5 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID_LENGTH(404, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    INVALID_DATE_OF_BIRTH(405, "Invalid date of birth", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(406, "Role not found", HttpStatus.NOT_FOUND),

    BOOK_NOT_FOUND(501, "Book not found", HttpStatus.NOT_FOUND),
    BOOK_EXISTED(502, "Book existed", HttpStatus.BAD_REQUEST),
    GENRE_NOT_FOUND(506, "Genre not found", HttpStatus.NOT_FOUND),




    UNAUTHENTICATED(801, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(802, "Unauthorized", HttpStatus.FORBIDDEN),
    INVALID_TOKEN(803, "Invalid token", HttpStatus.UNAUTHORIZED),

    INTERNAL_SERVER_ERROR(500, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

    int code;
    String message;
    HttpStatusCode httpStatus;

}
