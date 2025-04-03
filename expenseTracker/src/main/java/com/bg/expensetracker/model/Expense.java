package com.bg.expensetracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name="expense")
public class Expense {
    @Id
    private Long id;
    private Instant expensedate;
    private String description;
    private String location;
    @ManyToOne
    private Category category;

    @JsonIgnore
    @ManyToOne
    private User user;

}
