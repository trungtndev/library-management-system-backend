package com.example.springbootweb.entity;

import com.example.springbootweb.enums.LoanStatus;
import jakarta.persistence.*;
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
public class Loan {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    Book book;
    LocalDate loanDate;
    LocalDate dueDate;
    LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    LoanStatus status;
}
