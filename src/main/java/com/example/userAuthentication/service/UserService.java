package com.example.userAuthentication.service;

import com.example.userAuthentication.entity.User;
import com.example.userAuthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerOrUpdateUser(String email, String googleId, String name) {
        User user = userRepository.findByGoogleId(googleId);
        if (user == null) { // If user doesn't exist, create a new one
            user = new User();
            user.setEmail(email);
            user.setGoogleId(googleId);
            user.setName(name);
            return userRepository.save(user);
        }
        return user; // Return existing user
    }
}
