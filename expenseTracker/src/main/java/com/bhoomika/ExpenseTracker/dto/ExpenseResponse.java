// ExpenseResponse.java_v1
// DTO to return expense data to frontend

package com.bhoomika.ExpenseTracker.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ExpenseResponse {
    private Long expenseId;
    private String title;
    private Double totalAmount;
    private String currency;
    private Long groupId;
    private Long paidBy;
    private LocalDateTime date;
    private List<ExpenseParticipantResponse> participants;

    // Getters and Setters
    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(Long paidBy) {
        this.paidBy = paidBy;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<ExpenseParticipantResponse> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ExpenseParticipantResponse> participants) {
        this.participants = participants;
    }
}
