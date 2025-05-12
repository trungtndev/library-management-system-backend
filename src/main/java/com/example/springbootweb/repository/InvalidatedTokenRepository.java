package com.example.springbootweb.repository;

import com.example.springbootweb.entity.InvalidatedToken;
import com.example.springbootweb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
}
