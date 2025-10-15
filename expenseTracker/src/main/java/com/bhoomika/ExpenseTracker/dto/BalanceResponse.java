// BalanceResponse.java_v1
// DTO to represent a balance entry: who owes whom how much

package com.bhoomika.ExpenseTracker.dto;

public class BalanceResponse {
    private Long fromUserId;
    private Long toUserId;
    private Double amount; // amount that 'from' owes to 'to'

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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
