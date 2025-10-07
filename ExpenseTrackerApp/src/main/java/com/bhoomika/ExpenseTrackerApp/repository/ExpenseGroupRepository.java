package com.bhoomika.ExpenseTrackerApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bhoomika.ExpenseTrackerApp.entity.ExpenseGroup;

public interface ExpenseGroupRepository extends JpaRepository<ExpenseGroup, Long> {
    
}
