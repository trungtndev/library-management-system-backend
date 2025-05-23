package com.example.springbootweb.entity;

import com.example.springbootweb.enums.UserRole;
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
public class Role {
    @Id
    @Column(name = "name", unique = true, nullable = false, length = 32)
    String name;
    String description;

    @ManyToMany
    Set<Permission> permission;
}
