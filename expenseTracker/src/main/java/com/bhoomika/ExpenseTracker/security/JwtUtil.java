package com.bhoomika.ExpenseTracker.security;

// JwtUtil.java_v3
// Utility methods for JWT token creation and parsing
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import com.bhoomika.ExpenseTracker.model.User;

@Component
public class JwtUtil {

    public JwtUtil() {
        System.out.println("🏗️ JwtUtil: Bean constructed by Spring"); // Debug
    }
    @Value("${app.jwtSecret}")
    private String jwtSecret;
    
    // ✅ Decode once at startup
    private SecretKey SECRET_KEY;
    
    private final long EXPIRATION_TIME = 86400000; // 24 hours

    @PostConstruct
    public void init() {
        System.out.println("🔍 JwtUtil.init() called");
        System.out.println("   jwtSecret = '" + jwtSecret + "'");

        if (jwtSecret == null) {
            throw new IllegalArgumentException("❌ JWT secret is NULL. Check application.yml");
        }
        if (jwtSecret.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ JWT secret is empty");
        }
        System.out.println("✅ Raw jwtSecret: " + jwtSecret); // Debug
        try {
            this.SECRET_KEY = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(jwtSecret));
            System.out.println("✅ SecretKey initialized successfully");
        } catch (Exception e) {
            throw new IllegalArgumentException("❌ Failed to decode JWT secret. Must be valid Base64", e);
        }
    }
    
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUserId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long extractUserId(String token) {
        return Long.parseLong(extractAllClaims(token).getSubject());
    }

    public String extractRole(String token) {
        String role = (String) extractAllClaims(token).get("role");
        System.out.println("🔍 Extracted role from token: " + role);
        return role;
    }

    private Claims extractAllClaims(String token) {
        // return Jwts.parserBuilder() // ← Use parserBuilder() for JJWT 0.11.x
        //         .setSigningKey(SECRET_KEY)
        //         .build()
        //         .parseClaimsJws(token)
        //         .getBody();

                if (SECRET_KEY == null) {
                    System.err.println("❌ FATAL: secretKey is null during token parsing!");
                    throw new IllegalArgumentException("JWT signing key is null. Bean misconfigured.");
                }
                System.out.println("🔐 Using secretKey to parse token"); // Debug
                return Jwts.parserBuilder()
                        .setSigningKey(SECRET_KEY)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
    }
}

