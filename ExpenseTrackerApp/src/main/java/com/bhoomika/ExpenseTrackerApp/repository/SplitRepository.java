package com.bhoomika.ExpenseTrackerApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bhoomika.ExpenseTrackerApp.entity.Split;

public interface SplitRepository extends JpaRepository<Split, Long> {
    
}
