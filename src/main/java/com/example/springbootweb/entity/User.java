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
public class User {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String username;
    String password;
    String fullName;
    LocalDate dateOfBirth;
    String email;
    LocalDate createdAt;

    @ManyToMany
    Set<Role> role;
}