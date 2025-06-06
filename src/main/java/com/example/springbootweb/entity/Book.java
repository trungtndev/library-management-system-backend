package com.example.springbootweb.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
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

    String imageUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_storage_id", referencedColumnName = "id", nullable = false)
    FileStorage fileStorage;


    @ManyToMany
    Set<Genre> genre;
}
