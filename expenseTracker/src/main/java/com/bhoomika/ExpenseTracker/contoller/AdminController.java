package com.bhoomika.ExpenseTracker.contoller;

// AdminController.java_v3
// Admin-only endpoint for viewing all user profiles

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.bhoomika.ExpenseTracker.model.User;
import com.bhoomika.ExpenseTracker.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    // Endpoint only for admin users (backend service), not exposed to all
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        // return ResponseEntity.ok(userRepository.findAll());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("🛡️ Admin endpoint: " + auth);
        System.out.println("   Authorities: " + auth.getAuthorities());
        return ResponseEntity.ok(userRepository.findAll());
    }
}

