package com.bg.expensetracker.repository;

import com.bg.expensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepo extends JpaRepository<Expense, Long> {
}
