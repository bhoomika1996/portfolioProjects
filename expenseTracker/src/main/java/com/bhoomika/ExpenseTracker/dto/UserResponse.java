package com.bhoomika.ExpenseTracker.dto;

import lombok.Data;

@Data
public class UserResponse {
    private Long userId;
    private String name;
    private String email;

    // Getters and setters
}