package com.example.springbootweb.controller;

import com.example.springbootweb.dto.request.BookRequest;
import com.example.springbootweb.dto.respone.ApiResponse;
import com.example.springbootweb.dto.respone.BookResponse;
import com.example.springbootweb.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class BookController {
    BookService bookService;

    @PostMapping
    ApiResponse<BookResponse> createBook(@RequestBody BookRequest request) {
        BookResponse response = bookService.createBook(request);

        return ApiResponse.<BookResponse>builder()
                .success(true)
                .result(response)
                .build();
    }

    @GetMapping
    ApiResponse<List<BookResponse>> getAllBooks() {
        List<BookResponse> response = bookService.getAllBooks();

        return ApiResponse.<List<BookResponse>>builder()
                .success(true)
                .result(response)
                .build();
    }

    @GetMapping("/{bookId}")
    ApiResponse<BookResponse> getBookById(@PathVariable String bookId) {
        BookResponse response = bookService.getBookById(bookId);

        return ApiResponse.<BookResponse>builder()
                .success(true)
                .result(response)
                .build();
    }

    @PutMapping("/{bookId}")
    ApiResponse<BookResponse> updateBook(@PathVariable String bookId, @RequestBody BookRequest request) {
        BookResponse response = bookService.updateBook(bookId, request);

        return ApiResponse.<BookResponse>builder()
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

}
