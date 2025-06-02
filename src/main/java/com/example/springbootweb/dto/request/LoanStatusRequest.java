package com.example.springbootweb.dto.request;

import com.example.springbootweb.enums.LoanStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoanStatusRequest {
    LoanStatus status;
    LocalDate dueDate;
}
