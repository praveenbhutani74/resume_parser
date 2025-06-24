package com.resume.resume_parser.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resume.resume_parser.Entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    
    Optional<User> findByEmail(String email);

}
