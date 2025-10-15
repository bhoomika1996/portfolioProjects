package com.bhoomika.ExpenseTracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bhoomika.ExpenseTracker.model.ExpenseParticipant;
import com.bhoomika.ExpenseTracker.model.ExpenseParticipantId;

public interface ExpenseParticipantRepository extends JpaRepository<ExpenseParticipant, ExpenseParticipantId> {
     // Find all participants for a given expense
     List<ExpenseParticipant> findByExpenseExpenseId(Long expenseId);

    // ✅ Add this: Find all participants in a group
    @Query("SELECT ep FROM ExpenseParticipant ep WHERE ep.expense.group.groupId = :groupId")
    List<ExpenseParticipant> findByExpenseGroupGroupId(@Param("groupId") Long groupId);

    // Optional: Find by participant ID
    List<ExpenseParticipant> findByParticipantUserId(Long userId);
}
