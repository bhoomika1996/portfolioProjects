package com.bg.expensetracker.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="category")
@NoArgsConstructor
@Data
public class Category {
    @Id
    private Long id;

        //travel, entertainment, grocery, etc
    private String name;
}
