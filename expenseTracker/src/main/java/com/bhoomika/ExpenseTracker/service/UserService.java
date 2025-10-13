package com.bhoomika.ExpenseTracker.service;

// UserService.java_v1
// Business logic for user authentication and registration

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhoomika.ExpenseTracker.dto.UserRequest;
import com.bhoomika.ExpenseTracker.model.User;
import com.bhoomika.ExpenseTracker.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(UserRequest request) {
        String hash = passwordEncoder.encode(request.getPassword());
        //System.out.println("admin bcrypt password: "+passwordEncoder.encode("adminpass"));
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(hash);
        user.setRole("USER");
        return userRepository.save(user);
    }

    public User authenticateUser(UserRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                        .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }
        return user;
    }

    public User getUserProfile(Long userId) {
        return userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
    }
}

