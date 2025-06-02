package com.example.springbootweb.dto.respone;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PaginationResponse<T> {
    List<T> content;

    boolean first;
    boolean last;

    int totalPages;
    int number;
    int pageSize;

    long totalElements;
    long numberOfElements;
}