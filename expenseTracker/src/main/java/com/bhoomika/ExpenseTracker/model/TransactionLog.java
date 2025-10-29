package com.bhoomika.ExpenseTracker.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "transaction_logs")
@Data
public class TransactionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "from_user_id", nullable = false)
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id", nullable = false)
    private User toUser;

    private Double amount;

    private String description; // e.g., "Expense split update", "Payment settlement"
    
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
}
