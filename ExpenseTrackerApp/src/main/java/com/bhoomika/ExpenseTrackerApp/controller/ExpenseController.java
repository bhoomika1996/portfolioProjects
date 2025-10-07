package com.bhoomika.ExpenseTrackerApp.controller;

import com.bhoomika.ExpenseTrackerApp.dto.ExpenseDTO;
import com.bhoomika.ExpenseTrackerApp.entity.Expense;
import com.bhoomika.ExpenseTrackerApp.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody ExpenseDTO dto) {
        // For now, assume user is resolved from JWT (we'll add later)
        Expense expense = new Expense();
        expense.setAmount(dto.getAmount());
        expense.setDescription(dto.getDescription());
        expense.setDate(dto.getDate());
        // You'll set paidBy, category, and group after fetching from DB
        return ResponseEntity.ok(expenseService.addExpense(expense));
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Expense>> getExpensesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(expenseService.getExpensesByUserId(userId));
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Expense>> getExpensesByGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(expenseService.getExpensesByGroupId(groupId));
    }
}

