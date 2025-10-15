// GroupResponse.java_v1
// DTO to send group data back to frontend
// Avoids exposing full entity (security + clean API)

package com.bhoomika.ExpenseTracker.dto;

import java.time.LocalDateTime;
import java.util.List;

public class GroupResponse {
    private Long groupId;
    private String name;
    private Long createdBy;
    private LocalDateTime createdDate;
    private List<Long> memberIds;

    // Getters and Setters
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<Long> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }
}
