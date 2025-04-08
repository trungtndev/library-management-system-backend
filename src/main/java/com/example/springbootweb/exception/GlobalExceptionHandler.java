package com.example.springbootweb.exception;


import com.example.springbootweb.dto.respone.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { RuntimeException.class } )
    ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {

        ErrorCode errorCode = ErrorCode.UNCATEGORIZED;

        ApiResponse response = ApiResponse
                .builder()
                .code(errorCode.getCode())
//                .message(errorCode.getMessage())
                .message(ex.getMessage())
                .success(false)
                .build();


        return ResponseEntity.
                badRequest().
                body(response);
    }

    @ExceptionHandler(value = { AppException.class } )
    ResponseEntity<ApiResponse> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        ApiResponse response = ApiResponse
                .builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity
                .badRequest()
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


}
