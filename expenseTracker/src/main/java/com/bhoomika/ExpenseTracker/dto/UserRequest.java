package com.bhoomika.ExpenseTracker.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String name;
    private String email;
    private String password; // Plain password for registration

    // Getters and setters
}
