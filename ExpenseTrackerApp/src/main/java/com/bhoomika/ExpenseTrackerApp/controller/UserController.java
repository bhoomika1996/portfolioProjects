package com.bhoomika.ExpenseTrackerApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhoomika.ExpenseTrackerApp.entity.User;
import com.bhoomika.ExpenseTrackerApp.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired private UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
