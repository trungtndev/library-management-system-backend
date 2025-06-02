package com.example.springbootweb.controller;

import com.example.springbootweb.dto.request.ReviewCreationRequest;
import com.example.springbootweb.dto.request.ReviewUpdateRequest;
import com.example.springbootweb.dto.respone.ApiResponse;
import com.example.springbootweb.dto.respone.PaginationResponse;
import com.example.springbootweb.dto.respone.ReviewResponse;
import com.example.springbootweb.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {
    ReviewService reviewService;

    @PostMapping("books/{bookId}/reviews")
    ApiResponse<ReviewResponse> createReview(
            @PathVariable String bookId,
            @RequestBody ReviewCreationRequest request) {
        ReviewResponse response = reviewService.createReview(bookId, request);
        return ApiResponse.<ReviewResponse>builder()
                .result(response)
                .code(200)
                .success(true)
                .build();
    }

    @GetMapping("books/{bookId}/reviews")
    ApiResponse<PaginationResponse<ReviewResponse>> getAllReviews(
            @PathVariable String bookId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "rating") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        PaginationResponse<ReviewResponse> response = reviewService.getAllReviews(bookId, pageable);
        return ApiResponse.<PaginationResponse<ReviewResponse>>builder()
                .result(response)
                .code(200)
                .success(true)
                .build();
    }

    @PutMapping("reviews/{reviewId}")
    ApiResponse<ReviewResponse> updateReview(
            @PathVariable String reviewId,
            @RequestBody ReviewUpdateRequest request
    ) {
        ReviewResponse response = reviewService.updateReview(reviewId, request);
        return ApiResponse.<ReviewResponse>builder()
                .result(response)
                .code(200)
                .success(true)
                .build();
    }

    @DeleteMapping("reviews/{reviewId}")
    ApiResponse<Void> deleteReview(@PathVariable String reviewId) {
        reviewService.deleteReview(reviewId);
        return ApiResponse.<Void>builder()
                .success(true)
                .code(200)
                .build();
    }
}
