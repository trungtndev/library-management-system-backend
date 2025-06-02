package com.example.springbootweb.mapper;

import com.example.springbootweb.dto.request.BookCreationRequest;
import com.example.springbootweb.dto.respone.BookDetailResponse;
import com.example.springbootweb.dto.respone.BookSummaryResponse;
import com.example.springbootweb.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "fileStorage", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Book toBook(BookCreationRequest request);

    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "fileStorage", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateBook(@MappingTarget Book book, BookCreationRequest request);

    @Named("toBookDetailResponse")
    BookDetailResponse toBookDetailResponse(Book book);

    @Named("toBookSummaryResponse")
    BookSummaryResponse toBookSummaryResponse(Book book);

    @Named("toBookSummaryWithoutQuantity")
    @Mapping(target = "quantity", ignore = true)
    BookSummaryResponse toBookSummaryWithoutQuantity(Book book);
}
