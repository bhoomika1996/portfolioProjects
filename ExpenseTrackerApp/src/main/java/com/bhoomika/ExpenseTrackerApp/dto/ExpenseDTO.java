package com.bhoomika.ExpenseTrackerApp.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ExpenseDTO {
    private Double amount;
    private String description;
    private LocalDate date;
    private String categoryName;
    private Long groupId;
}
