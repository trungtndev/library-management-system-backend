package com.example.springbootweb.dto.respone;

import com.example.springbootweb.enums.LoanStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanDetailResponse {
    String id;
    UserSummaryResponse user;
    BookSummaryResponse book;
    LoanStatus status;
    LocalDate loanDate;
    LocalDate dueDate;
    LocalDate returnDate;
}
