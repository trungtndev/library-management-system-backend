package com.example.springbootweb.dto.respone;

import com.example.springbootweb.entity.Genre;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookResponse {
    String id;
    String title;
    String author;
    String description;
    int quantity;
    LocalDate createdAt;
    Set<Genre> genre;
}
