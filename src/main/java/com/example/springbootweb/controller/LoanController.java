package com.example.springbootweb.controller;

import com.example.springbootweb.dto.request.LoanCreationRequest;
import com.example.springbootweb.dto.request.LoanStatusRequest;
import com.example.springbootweb.dto.respone.ApiResponse;
import com.example.springbootweb.dto.respone.LoanDetailResponse;
import com.example.springbootweb.dto.respone.LoanSummaryResponse;
import com.example.springbootweb.dto.respone.PaginationResponse;
import com.example.springbootweb.service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class LoanController {
    LoanService loanService;

    @PostMapping("/books/{bookId}/loans")
    ApiResponse<LoanDetailResponse> createLoan(
            @PathVariable String bookId,
            @RequestBody LoanCreationRequest request) {
        LoanDetailResponse response = loanService.createLoan(bookId, request);
        return ApiResponse.<LoanDetailResponse>builder()
                .success(true)
                .result(response)
                .build();
    }

    @GetMapping("/loans")
    ApiResponse<PaginationResponse<LoanSummaryResponse>> getAllLoans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        PaginationResponse<LoanSummaryResponse> response = loanService.getAllLoans(pageable);
        return ApiResponse.<PaginationResponse<LoanSummaryResponse>>builder()
                .success(true)
                .result(response)
                .build();
    }

    @GetMapping("/loans/{loanId}")
    ApiResponse<LoanDetailResponse> getLoanById(@PathVariable String loanId) {
        LoanDetailResponse response = loanService.getLoanById(loanId);
        return ApiResponse.<LoanDetailResponse>builder()
                .success(true)
                .result(response)
                .build();
    }

    @GetMapping("/loans/me")
    ApiResponse<PaginationResponse<LoanSummaryResponse>> getMyLoans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        PaginationResponse<LoanSummaryResponse> response = loanService.getMyLoans(pageable);
        return ApiResponse.<PaginationResponse<LoanSummaryResponse>>builder()
                .success(true)
                .result(response)
                .build();
    }

    @DeleteMapping("/loans/{loanId}")
    ApiResponse<String> deleteLoan(@PathVariable String loanId) {
        loanService.deleteLoan(loanId);
        return ApiResponse.<String>builder()
                .success(true)
                .build();
    }

    @PutMapping("/loans/{loanId}/status")
    ApiResponse<LoanDetailResponse> updateLoanStatus(@PathVariable String loanId, @RequestBody LoanStatusRequest request) {
        LoanDetailResponse response = loanService.updateLoanStatus(loanId, request);
        return ApiResponse.<LoanDetailResponse>builder()
                .success(true)
                .result(response)
                .build();
    }

    @GetMapping("/users/{userId}/loans")
    ApiResponse<PaginationResponse<LoanSummaryResponse>> getLoansByUserId(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        PaginationResponse<LoanSummaryResponse> response = loanService.getLoansByUserId(userId, pageable);
        return ApiResponse.<PaginationResponse<LoanSummaryResponse>>builder()
                .success(true)
                .result(response)
                .build();
    }
}
