package com.bhoomika.ExpenseTrackerApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bhoomika.ExpenseTrackerApp.entity.Split;

public interface SplitRepository extends JpaRepository<Split, Long> {
    List<Split> findByExpense_Group_IdAndSettledFalse(Long groupId);
}
