package com.bg.expensetracker.controller;

import com.bg.expensetracker.model.Expense;
import com.bg.expensetracker.repository.ExpenseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ExpenseController {
    @Autowired
    private ExpenseRepo expenseRepo;

    @GetMapping("/expenses")
    List<Expense> getAllExpenses() {
        return expenseRepo.findAll();
    }

    @DeleteMapping("/expenses/{id}")
    ResponseEntity<?> deleteExpense(@PathVariable Long id) {
        Optional<Expense> expense = expenseRepo.findById(id);
        if (expense.isPresent()) {
            expenseRepo.deleteById(id);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/expenses")
    ResponseEntity<?> addExpense(@RequestBody Expense expense) {
        expenseRepo.save(expense);
        return ResponseEntity.noContent().build();
    }
}
