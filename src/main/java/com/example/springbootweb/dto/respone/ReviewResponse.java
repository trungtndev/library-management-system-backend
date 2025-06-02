package com.example.springbootweb.dto.respone;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {
    String id;
    String fullName;
    int rating;
    String comment;
    LocalDate createdAt;

    boolean isMine;
}
