package com.example.springbootweb.service;

import com.example.springbootweb.dto.request.BookCreationRequest;
import com.example.springbootweb.dto.respone.BookDetailResponse;
import com.example.springbootweb.dto.respone.BookSummaryResponse;
import com.example.springbootweb.dto.respone.PaginationResponse;
import com.example.springbootweb.entity.Book;
import com.example.springbootweb.entity.FileStorage;
import com.example.springbootweb.exception.AppException;
import com.example.springbootweb.exception.ErrorCode;
import com.example.springbootweb.mapper.BookMapper;
import com.example.springbootweb.mapper.PaginationMapper;
import com.example.springbootweb.repository.BookRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookService {
    String baseUrl;
    FileStorageService fileStorageService;
    BookRepository bookRepository;
    BookMapper bookMapper;
    PaginationMapper paginationMapper;

    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    @Transactional
    public BookDetailResponse createBook(BookCreationRequest request, MultipartFile image) throws IOException {
        Book book = bookMapper.toBook(request);

        FileStorage fileStorage = null;
        if (image != null && !image.isEmpty()) {
            fileStorage = fileStorageService.storeFile(image);
            String url = baseUrl + "/files/" + fileStorage.getId();
            book.setImageUrl(url);
        }

        book.setCreatedAt(LocalDate.now());
        book.setFileStorage(fileStorage);
        book = bookRepository.save(book);


        BookDetailResponse response = bookMapper.toBookDetailResponse(book);
//        if (book.getFileStorage() != null) {
//            FileStorage file = book.getFileStorage();
//            String imageUrl = baseUrl + "/files/" + file.getId();
//            response.setImageUrl(imageUrl);
//        }
        return response;
    }


    public PaginationResponse<BookSummaryResponse> getAllBooks(Pageable pageable, String search) {
        Page<Book> books;

        if (search != null && !search.isEmpty()) {
            books = bookRepository.findAllByKeyword(search, pageable);
        } else {
            books = bookRepository.findAll(pageable);

        }

        Page<BookSummaryResponse> responses = books.map(bookMapper::toBookSummaryResponse);
        return paginationMapper.toPaginationResponse(responses);
    }

//    public Page<BookSummaryResponse> getAllBooks(Pageable pageable) {
//
//        return bookRepository.findAll(pageable)
//                .map(bookMapper::toBookSummaryResponse);
//    }

    public BookDetailResponse getBookById(String bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        return bookMapper.toBookDetailResponse(book);
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public BookDetailResponse updateBook(String bookId, BookCreationRequest request) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        bookMapper.updateBook(book, request);

        try {
            book = bookRepository.save(book);
        } catch (Exception e) {
            throw new AppException(ErrorCode.GENRE_NOT_FOUND);
        }
        return bookMapper.toBookDetailResponse(book);
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public void deleteBook(String bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        bookRepository.delete(book);
    }
}
