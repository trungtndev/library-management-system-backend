package com.example.springbootweb.mapper;

import com.example.springbootweb.dto.request.GenreRequest;
import com.example.springbootweb.dto.respone.GenreResponse;
import com.example.springbootweb.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    Genre toGenre(GenreRequest request);
    void updateGenre(@MappingTarget Genre genre, GenreRequest request);
    GenreResponse toGenreResponse(Genre genre);
}
