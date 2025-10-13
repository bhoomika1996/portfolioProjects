// src/main/java/com/bhoomika/ExpenseTrackerApp/dto/ExpenseDTO.java

package com.bhoomika.ExpenseTrackerApp.dto;

import java.time.LocalDate;
import java.util.List;

import com.bhoomika.ExpenseTrackerApp.entity.Expense;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExpenseDTO {

    private Double amount;
    private String description;
    private LocalDate date;
    private String categoryName;
    private Long paidById;
    private Long groupId;
    private List<String> splitAmongUsernames;

    // ✅ Constructor from Expense entity (required for .map(ExpenseDTO::new))
    public ExpenseDTO(Expense expense) {
        this.amount = expense.getAmount();
        this.description = expense.getDescription();
        this.date = expense.getDate();
        this.categoryName = expense.getCategory() != null ? expense.getCategory().getName() : null;
        this.paidById = expense.getPaidBy() != null ? expense.getPaidBy().getId() : null;
        this.groupId = expense.getGroup() != null ? expense.getGroup().getId() : null;
    }

    // Getters and Setters
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public Long getPaidById() { return paidById; }
    public void setPaidById(Long paidById) { this.paidById = paidById; }

    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }
    public void setSplitAmongUsernames(List<String> splitAmongUsernames) { this.splitAmongUsernames = splitAmongUsernames; }
    public List<String> getSplitAmongUsernames() { return splitAmongUsernames; }
}
