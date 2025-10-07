package com.bhoomika.ExpenseTrackerApp.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // will store the encoded password

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles; // e.g., ["USER"], ["ADMIN"]
}

