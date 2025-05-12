package com.example.springbootweb.dto.request;

import com.example.springbootweb.entity.Genre;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GenreRequest {
    String name;
    String description;
}
