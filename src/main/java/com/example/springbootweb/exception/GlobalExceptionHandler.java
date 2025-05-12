package com.example.springbootweb.exception;


import com.example.springbootweb.dto.respone.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = { RuntimeException.class } )
//    ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {
//
//        ErrorCode errorCode = ErrorCode.UNCATEGORIZED;
//
//        ApiResponse response = ApiResponse
//                .builder()
//                .code(errorCode.getCode())
////                .message(errorCode.getMessage())
//                .message(ex.getMessage())
//                .success(false)
//                .build();
//
//
//        return ResponseEntity
//                .badRequest()
//                .body(response);
//    }

    @ExceptionHandler(value = { AppException.class } )
    ResponseEntity<ApiResponse> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        ApiResponse response = ApiResponse
                .builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(response);
    }


    @ExceptionHandler(value = { MethodArgumentNotValidException.class } )
    ResponseEntity<ApiResponse> handleHandleValidation(MethodArgumentNotValidException ex) {
        String enumKey = Objects.requireNonNull(ex.getFieldError()).getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);

        ApiResponse response = ApiResponse
                .builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity
                .badRequest()
                .body(response);
    }


    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
}
