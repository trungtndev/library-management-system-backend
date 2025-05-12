package com.example.springbootweb.dto.request;

import com.example.springbootweb.entity.Genre;
import jakarta.persistence.ManyToMany;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookRequest {
    String title;
    String author;
    String description;
    int quantity;
    Set<Genre> genre;
}
