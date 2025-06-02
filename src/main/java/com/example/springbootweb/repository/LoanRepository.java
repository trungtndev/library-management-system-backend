package com.example.springbootweb.repository;

import com.example.springbootweb.entity.Loan;
import com.example.springbootweb.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface LoanRepository extends JpaRepository<Loan, String> {

    Collection<Loan> findAllByUser(User user);

    Page<Loan> findAllByUser(User user, Pageable pageable);
}
