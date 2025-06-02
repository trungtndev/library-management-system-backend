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
    BOOK_NOT_AVAILABLE(503, "Book not available", HttpStatus.BAD_REQUEST),
    GENRE_NOT_FOUND(506, "Genre not found", HttpStatus.NOT_FOUND),


    INVALID_DUE_DATE(601, "Invalid due date", HttpStatus.BAD_REQUEST),
    LOAN_NOT_FOUND(602, "Loan not found", HttpStatus.NOT_FOUND),
    INVALID_STATUS_CHANGE(603, "Invalid status change", HttpStatus.BAD_REQUEST),
    LOAN_STATUS_ALREADY_APPROVED(604, "Cannot change status of approved loan", HttpStatus.BAD_REQUEST),
    DUE_DATE_NULL(605, "Due date cannot be null", HttpStatus.BAD_REQUEST),
    USER_ID_NULL(606, "User id cannot be null", HttpStatus.BAD_REQUEST),

    RATING_INVALID(701, "Rating must be between 1 and 5", HttpStatus.BAD_REQUEST),
    REVIEW_ALREADY_EXISTS(702, "Review already exists", HttpStatus.BAD_REQUEST),
    REVIEW_NOT_FOUND(703, "Review not found", HttpStatus.NOT_FOUND),


    UNAUTHENTICATED(801, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(802, "Unauthorized", HttpStatus.FORBIDDEN),
    INVALID_TOKEN(803, "Invalid token", HttpStatus.UNAUTHORIZED),
    DO_NOT_HAVE_PERMISSION(804, "Do not have permission", HttpStatus.FORBIDDEN),
    ACCESS_DENIED(805, "Access denied", HttpStatus.FORBIDDEN),
    INTERNAL_SERVER_ERROR(500, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

    int code;
    String message;
    HttpStatusCode httpStatus;

}
