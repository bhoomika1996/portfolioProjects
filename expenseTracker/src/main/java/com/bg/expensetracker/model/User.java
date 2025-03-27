package com.bg.expensetracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name="users")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;

    @OneToMany
    private Set<Category> category;
}
