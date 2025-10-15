// ExpenseController.java_v1
// REST API for managing expenses
// Handles creation, retrieval, and split logic

package com.bhoomika.ExpenseTracker.contoller;

import com.bhoomika.ExpenseTracker.dto.ExpenseRequest;
import com.bhoomika.ExpenseTracker.dto.ExpenseResponse;
import com.bhoomika.ExpenseTracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    /**
     * Create a new expense with split
     * POST /api/expenses
     * Request Body: See ExpenseRequest
     */
    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@RequestBody ExpenseRequest request) {
        ExpenseResponse expense = expenseService.createExpense(request);
        return ResponseEntity.ok(expense);
    }

    /**
     * Get all expenses in a group
     * GET /api/expenses/group/{groupId}
     */
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByGroup(@PathVariable Long groupId) {
        List<ExpenseResponse> expenses = expenseService.getExpensesByGroup(groupId);
        return ResponseEntity.ok(expenses);
    }

    /**
     * Get all expenses paid by a user
     * GET /api/expenses/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByUser(@PathVariable Long userId) {
        List<ExpenseResponse> expenses = expenseService.getExpensesByUser(userId);
        return ResponseEntity.ok(expenses);
    }
}
