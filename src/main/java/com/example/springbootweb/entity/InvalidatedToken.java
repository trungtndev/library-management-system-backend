package com.example.springbootweb.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class InvalidatedToken {
    @Id
    String id; // token id
    Date expiryTime;

}
