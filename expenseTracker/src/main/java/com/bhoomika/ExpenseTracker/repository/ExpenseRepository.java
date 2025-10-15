package com.bhoomika.ExpenseTracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.bhoomika.ExpenseTracker.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
    @Query("SELECT e FROM Expense e WHERE e.group.groupId = :groupId")
    List<Expense> findByGroupId(@Param("groupId") Long groupId);

   // List<Expense> findByGroupId(Long groupId);

    @Query("SELECT e FROM Expense e JOIN e.participants p WHERE p.participant.userId = :userId")
    List<Expense> findByParticipantId(@Param("userId") Long userId);
}
