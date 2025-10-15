package com.bhoomika.ExpenseTracker.model;

import java.io.Serializable;
import java.util.Objects;

// Composite key for expense_participants table: (expense_id, participant_id)
public class ExpenseParticipantId implements Serializable {
    private Long expense;
    private Long participant;

    public ExpenseParticipantId() {}

    public ExpenseParticipantId(Long expense, Long participant) {
        this.expense = expense;
        this.participant = participant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpenseParticipantId)) return false;
        ExpenseParticipantId that = (ExpenseParticipantId) o;
        return Objects.equals(expense, that.expense) && Objects.equals(participant, that.participant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expense, participant);
    }
}
