package com.example.springbootweb.repository;

import com.example.springbootweb.entity.Book;
import com.example.springbootweb.entity.Review;
import com.example.springbootweb.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    boolean existsByUserAndBook(User user, Book book);

    List<Review> findAllByBookId(String bookId);

    Page<Review> findAllByBookId(String bookId, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.book.id = :bookId ORDER BY " +
            "CASE WHEN r.user.id = :currentUserId THEN 0 ELSE 1 END, " +
            "r.createdAt DESC")
    Page<Review> findAllByBookIdOrderByMineFirst(@Param("bookId") String bookId,
                                                 @Param("currentUserId") String currentUserId,
                                                 Pageable pageable);

}
