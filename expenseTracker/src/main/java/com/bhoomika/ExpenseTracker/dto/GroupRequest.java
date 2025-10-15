// GroupRequest.java_v1
// Data Transfer Object for group creation and member updates
// Used to receive input from frontend

package com.bhoomika.ExpenseTracker.dto;

import java.util.List;

public class GroupRequest {
    private String name;
    private List<Long> memberIds; // List of user IDs to add

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }
}
