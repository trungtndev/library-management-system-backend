package com.example.springbootweb.controller;

import com.example.springbootweb.dto.request.GenreRequest;
import com.example.springbootweb.dto.respone.ApiResponse;
import com.example.springbootweb.dto.respone.GenreResponse;
import com.example.springbootweb.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genre")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class GenreController {
    GenreService genreService;

    @PostMapping
    ApiResponse<GenreResponse> createGenre(@RequestBody GenreRequest request) {
        GenreResponse response = genreService.createGenre(request);

        return ApiResponse.<GenreResponse>builder()
                .success(true)
                .result(response)
                .build();
    }

    @GetMapping
    ApiResponse<List<GenreResponse>> getAllGenre() {
        List<GenreResponse> response = genreService.getAllGenres();

        return ApiResponse.<List<GenreResponse>>builder()
                .success(true)
                .result(response)
                .build();
    }

    @GetMapping("/{genreId}")
    ApiResponse<GenreResponse> getGenreById(@PathVariable String genreId) {
        GenreResponse response = genreService.getGenreById(genreId);

        return ApiResponse.<GenreResponse>builder()
                .success(true)
                .result(response)
                .build();
    }

    @PutMapping("/{genreId}")
    ApiResponse<GenreResponse> updateGenre(@PathVariable String genreId, @RequestBody GenreRequest request) {
        GenreResponse response = genreService.updateGenre(genreId, request);

        return ApiResponse.<GenreResponse>builder()
                .success(true)
                .result(response)
                .build();
    }

    @DeleteMapping("/{genreId}")
    ApiResponse<Void> deleteGenre(@PathVariable String genreId) {
        genreService.deleteGenre(genreId);

        return ApiResponse.<Void>builder()
                .success(true)
                .build();
    }

}
