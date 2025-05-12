package com.example.springbootweb.service;

import com.example.springbootweb.dto.request.BookRequest;
import com.example.springbootweb.dto.request.GenreRequest;
import com.example.springbootweb.dto.respone.BookResponse;
import com.example.springbootweb.dto.respone.GenreResponse;
import com.example.springbootweb.entity.Book;
import com.example.springbootweb.entity.Genre;
import com.example.springbootweb.exception.AppException;
import com.example.springbootweb.exception.ErrorCode;
import com.example.springbootweb.mapper.BookMapper;
import com.example.springbootweb.mapper.GenreMapper;
import com.example.springbootweb.repository.BookRepository;
import com.example.springbootweb.repository.GenreRepository;
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
public class GenreService {
    GenreRepository genreRepository;
    GenreMapper genreMapper;

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public GenreResponse createGenre(GenreRequest request) {
        Genre genre = genreMapper.toGenre(request);

        return genreMapper.toGenreResponse(genreRepository.save(genre));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public List<GenreResponse> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();

        return genres.stream()
                .map(genreMapper::toGenreResponse)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public GenreResponse getGenreById(String genreId) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new AppException(ErrorCode.GENRE_NOT_FOUND));

        return genreMapper.toGenreResponse(genre);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public GenreResponse updateGenre(String genreId, GenreRequest request) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new AppException(ErrorCode.GENRE_NOT_FOUND));

        genreMapper.updateGenre(genre, request);

        return genreMapper.toGenreResponse(genreRepository.save(genre));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public void deleteGenre(String genreId) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new AppException(ErrorCode.GENRE_NOT_FOUND));

        genreRepository.delete(genre);
    }
}
