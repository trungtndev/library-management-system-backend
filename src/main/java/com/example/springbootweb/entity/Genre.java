package com.example.springbootweb.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Genre {
    @Id
    @Column(name = "name", unique = true, nullable = false)
    String name;
    String description;
}
