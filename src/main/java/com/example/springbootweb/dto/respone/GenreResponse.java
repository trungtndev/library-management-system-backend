package com.example.springbootweb.dto.respone;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GenreResponse {
    String name;
    String description;
}
