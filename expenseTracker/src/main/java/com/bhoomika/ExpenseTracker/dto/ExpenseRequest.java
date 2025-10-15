// ExpenseRequest.java_v1
// DTO for creating an expense
// Includes split type and optional maps for exact/percentage splits

package com.bhoomika.ExpenseTracker.dto;

import java.util.Map;

import com.bhoomika.ExpenseTracker.model.SplitType;

public class ExpenseRequest {
    private Long groupId;
    private Long paidBy;
    private String title;
    private Double totalAmount;
    private String currency;
    private SplitType splitType;
    private Map<Long, Double> exactAmounts;  // userId -> amount
    private Map<Long, Double> percentages;  // userId -> percentage

    // Getters and Setters
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

    public SplitType getSplitType() {
        return splitType;
    }

    public void setSplitType(SplitType splitType) {
        this.splitType = splitType;
    }

    public Map<Long, Double> getExactAmounts() {
        return exactAmounts;
    }

    public void setExactAmounts(Map<Long, Double> exactAmounts) {
        this.exactAmounts = exactAmounts;
    }

    public Map<Long, Double> getPercentages() {
        return percentages;
    }

    public void setPercentages(Map<Long, Double> percentages) {
        this.percentages = percentages;
    }
}

