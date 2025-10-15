// Balance.java_v4
package com.bhoomika.ExpenseTracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "balances")
@Data
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long balanceId;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @Column(nullable = false)
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}
