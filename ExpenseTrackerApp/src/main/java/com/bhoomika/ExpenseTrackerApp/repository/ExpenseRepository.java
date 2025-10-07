package com.bhoomika.ExpenseTrackerApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bhoomika.ExpenseTrackerApp.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
}
