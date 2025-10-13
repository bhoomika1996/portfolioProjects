package com.bhoomika.ExpenseTrackerApp.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
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
    private Role roles; // e.g., ["USER"], ["ADMIN"]

    @ManyToMany(mappedBy = "members")
    private Set<ExpenseGroup> groups = new HashSet<>();

    public void addGroup(ExpenseGroup group) {
        groups.add(group);
        group.getMembers().add(this);
    }
    
    public void removeGroup(ExpenseGroup group) {
        groups.remove(group);
        group.getMembers().remove(this);
    }
    

}

