package com.bhoomika.ExpenseTrackerApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bhoomika.ExpenseTrackerApp.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByPaidById(Long userId);
    List<Expense> findByGroup_Id(Long groupId);
}
