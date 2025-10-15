// PaymentRequest.java_v1
// DTO for recording a payment

package com.bhoomika.ExpenseTracker.dto;

public class PaymentRequest {
    private Long fromUserId;
    private Long toUserId;
    private Long groupId;
    private Double amount;

    // Getters and Setters
    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
