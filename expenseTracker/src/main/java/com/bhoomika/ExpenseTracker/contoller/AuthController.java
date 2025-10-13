package com.bhoomika.ExpenseTracker.contoller;

// AuthController.java_v1
// Handles registration and login
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bhoomika.ExpenseTracker.dto.UserRequest;
import com.bhoomika.ExpenseTracker.model.User;
import com.bhoomika.ExpenseTracker.security.JwtUtil;
import com.bhoomika.ExpenseTracker.service.UserService;

import org.springframework.http.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // Registration endpoint
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest request) {
        User newUser = userService.registerUser(request);
        return ResponseEntity.ok("User registered: " + newUser.getEmail());
    }

    // Login endpoint - returns JWT token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest request) {
        User user = userService.authenticateUser(request);
        String jwt = jwtUtil.generateToken(user);
        return ResponseEntity.ok(jwt);
    }

    // Profile details endpoint (JWT protected)
    @GetMapping("/profile")
    public ResponseEntity<User> profile(@RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        User user = userService.getUserProfile(userId);
        return ResponseEntity.ok(user);
    }
}

