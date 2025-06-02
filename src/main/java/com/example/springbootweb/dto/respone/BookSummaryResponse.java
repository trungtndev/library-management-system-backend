package com.example.springbootweb.dto.respone;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookSummaryResponse {
    String id;
    String title;
    Integer quantity;
    String imageUrl;
}
