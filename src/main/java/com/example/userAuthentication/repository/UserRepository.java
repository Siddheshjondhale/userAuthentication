package com.example.userAuthentication.repository;

import com.example.userAuthentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByGoogleId(String googleId); // New method for finding by Google ID
}
