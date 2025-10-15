// ExpenseParticipantResponse.java_v1
// DTO for participant details in expense response

package com.bhoomika.ExpenseTracker.dto;

public class ExpenseParticipantResponse {
    private Long userId;
    private Double shareAmount;
    private String splitType;

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(Double shareAmount) {
        this.shareAmount = shareAmount;
    }

    public String getSplitType() {
        return splitType;
    }

    public void setSplitType(String splitType) {
        this.splitType = splitType;
    }
}
