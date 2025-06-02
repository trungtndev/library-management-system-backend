package com.example.springbootweb.controller;

import com.example.springbootweb.dto.request.BookCreationRequest;
import com.example.springbootweb.dto.respone.ApiResponse;
import com.example.springbootweb.dto.respone.BookDetailResponse;
import com.example.springbootweb.dto.respone.BookSummaryResponse;
import com.example.springbootweb.dto.respone.PaginationResponse;
import com.example.springbootweb.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class BookController {
    BookService bookService;

    @PostMapping
    ApiResponse<BookDetailResponse> createBook(
            @RequestPart("request") String request,
            @RequestParam("image") MultipartFile image
    ) throws IOException {
        BookDetailResponse response = bookService.createBook(convertToJsonRequest(request), image);

        return ApiResponse.<BookDetailResponse>builder()
                .success(true)
                .result(response)
                .build();
    }

    @GetMapping
    ApiResponse<PaginationResponse<BookSummaryResponse>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "title") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        PaginationResponse<BookSummaryResponse> response = bookService.getAllBooks(pageable, search);
        return ApiResponse.<PaginationResponse<BookSummaryResponse>>builder()
                .success(true)
                .result(response)
                .build();
    }
//    @GetMapping
//    ApiResponse<Page<BookSummaryResponse>> getAllBooks(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size,
//            @RequestParam(defaultValue = "title") String sortBy
//    ) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
//        Page<BookSummaryResponse> response = bookService.getAllBooks(pageable);
//        return ApiResponse.<Page<BookSummaryResponse>>builder()
//                .success(true)
//                .result(response)
//                .build();
//    }

    @GetMapping("/{bookId}")
    ApiResponse<BookDetailResponse> getBookById(@PathVariable String bookId) {
        BookDetailResponse response = bookService.getBookById(bookId);

        return ApiResponse.<BookDetailResponse>builder()
                .success(true)
                .result(response)
                .build();
    }

    @PutMapping("/{bookId}")
    ApiResponse<BookDetailResponse> updateBook(@PathVariable String bookId, @RequestBody BookCreationRequest request) {
        BookDetailResponse response = bookService.updateBook(bookId, request);

        return ApiResponse.<BookDetailResponse>builder()
                .success(true)
                .result(response)
                .build();
    }

    @DeleteMapping("/{bookId}")
    ApiResponse<Void> deleteBook(@PathVariable String bookId) {
        bookService.deleteBook(bookId);

        return ApiResponse.<Void>builder()
                .success(true)
                .build();
    }

    private BookCreationRequest convertToJsonRequest(String request) throws JsonProcessingException {
        // Implement the logic to convert the request string to BookCreationRequest object
        // This is a placeholder method and should be replaced with actual implementation
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(request, BookCreationRequest.class);

    }

}
