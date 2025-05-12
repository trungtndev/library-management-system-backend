package com.example.springbootweb.entity;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Book {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String title;
    String author;
    String description;
    int quantity;
    LocalDate createdAt;

    @ManyToMany
    Set<Genre> genre;
}
