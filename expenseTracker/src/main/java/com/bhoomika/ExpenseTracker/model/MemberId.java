// MemberId.java (as a separate file, best for JPA)
package com.bhoomika.ExpenseTracker.model;

import java.io.Serializable;
import java.util.Objects;

public class MemberId implements Serializable {
    private Long groupId;
    private Long userId;

    public MemberId() {} // Default constructor required

    public MemberId(Long groupId, Long userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberId)) return false;
        MemberId that = (MemberId) o;
        return Objects.equals(groupId, that.groupId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, userId);
    }
}
