package com.example.springbootweb.mapper;

import com.example.springbootweb.dto.request.BookRequest;
import com.example.springbootweb.dto.respone.BookResponse;
import com.example.springbootweb.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Book toBook(BookRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateBookFromRequest(BookRequest request, @MappingTarget Book book);

    BookResponse toBookResponse(Book book);
}
