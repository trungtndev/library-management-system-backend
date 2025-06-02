package com.example.springbootweb.dto.respone;

import com.example.springbootweb.enums.LoanStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoanSummaryResponse {
    String id;
    UserSummaryResponse user;
    BookSummaryResponse book;
    LoanStatus status;
    LocalDate dueDate;
    LocalDate loanDate;
}
