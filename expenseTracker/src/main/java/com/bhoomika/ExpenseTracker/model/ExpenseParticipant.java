// ExpenseParticipant.java_v4
package com.bhoomika.ExpenseTracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "expense_participants")
@IdClass(ExpenseParticipantId.class)
@Data
public class ExpenseParticipant {
    @Id
    @ManyToOne
    @JoinColumn(name = "expense_id")
    private Expense expense;

    @Id
    @ManyToOne
    @JoinColumn(name = "participant_id") // User making up the split
    private User participant;

    @Column(name = "share_amount", nullable = false)
    private Double shareAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "split_type", nullable = false)
    private SplitType splitType;

    // Getters and setters...
}

// class ExpenseParticipantId implements Serializable {
//     private Long expense;
//     private Long participant;
// }

