package com.example.springbootweb.mapper;

import com.example.springbootweb.dto.respone.PaginationResponse;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface PaginationMapper {
    default <T> PaginationResponse<T> toPaginationResponse(Page<T> page) {
        return PaginationResponse.<T>builder()
                .first(page.isFirst())
                .last(page.isLast())

                .content(page.getContent())

                .totalPages(page.getTotalPages())
                .number(page.getNumber())
                .pageSize(page.getSize())

                .totalElements(page.getTotalElements())
                .numberOfElements(page.getNumberOfElements())
                .build();
    }
}
