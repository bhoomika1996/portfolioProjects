package com.bhoomika.ExpenseTrackerApp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double amount;
    private String description;
    private LocalDate date;

    @ManyToOne
    private User paidBy;

    @ManyToOne
    private Category category;

    @ManyToOne
    private ExpenseGroup group; // nullable, null if personal
}
