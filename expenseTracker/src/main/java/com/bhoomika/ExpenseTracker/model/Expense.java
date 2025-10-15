package com.bhoomika.ExpenseTracker.model;

// Expense.java_v4

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "expenses")
@Data
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private String currency;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne
    @JoinColumn(name = "paid_by", nullable = false)
    private User paidBy;

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now();

    // All participants for this expense
    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
    private List<ExpenseParticipant> participants;

    // Getters and setters...
}

