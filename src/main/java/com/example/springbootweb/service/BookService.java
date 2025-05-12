package com.example.springbootweb.service;

import com.example.springbootweb.dto.request.BookRequest;
import com.example.springbootweb.dto.respone.BookResponse;
import com.example.springbootweb.entity.Book;
import com.example.springbootweb.exception.AppException;
import com.example.springbootweb.exception.ErrorCode;
import com.example.springbootweb.mapper.BookMapper;
import com.example.springbootweb.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookService {

    BookRepository bookRepository;
    BookMapper bookMapper;

    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public BookResponse createBook(BookRequest request) {
        Book book = bookMapper.toBook(request);
        book.setCreatedAt(LocalDate.now());

        try {
            book = bookRepository.save(book);
        } catch (EntityNotFoundException e) {
            throw new AppException(ErrorCode.GENRE_NOT_FOUND);
        }

        return bookMapper.toBookResponse(book);
    }


    public List<BookResponse> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
    }

    public BookResponse getBookById(String bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        return bookMapper.toBookResponse(book);
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public BookResponse updateBook(String bookId, BookRequest request) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        bookMapper.updateBookFromRequest(request, book);

        try {
            book = bookRepository.save(book);
        } catch (Exception e) {
            throw new AppException(ErrorCode.GENRE_NOT_FOUND);
        }
        return bookMapper.toBookResponse(book);
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public void deleteBook(String bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        bookRepository.delete(book);
    }
}
