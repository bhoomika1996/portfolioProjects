package com.bhoomika.ExpenseTrackerApp.controller;

import com.bhoomika.ExpenseTrackerApp.dto.ExpenseDTO;
import com.bhoomika.ExpenseTrackerApp.entity.Category;
import com.bhoomika.ExpenseTrackerApp.entity.Expense;
import com.bhoomika.ExpenseTrackerApp.entity.ExpenseGroup;
import com.bhoomika.ExpenseTrackerApp.entity.User;
import com.bhoomika.ExpenseTrackerApp.repository.CategoryRepository;
import com.bhoomika.ExpenseTrackerApp.repository.ExpenseGroupRepository;
import com.bhoomika.ExpenseTrackerApp.repository.UserRepository;
import com.bhoomika.ExpenseTrackerApp.service.ExpenseGroupService;
import com.bhoomika.ExpenseTrackerApp.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
// @CrossOrigin
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;
    
    
    // @PostMapping("/expenses")
    // public ResponseEntity<ExpenseDTO> addExpense(@RequestBody ExpenseDTO dto) {
    //     // Resolve paidBy
    //     User paidBy = userRepository.findById(dto.getPaidById())
    //             .orElseThrow(() -> new RuntimeException("Payer not found"));

    //     // Resolve group
    //     ExpenseGroup group = null;
    //     if (dto.getGroupId() != null) {
    //         group = expenseGroupRepository.findById(dto.getGroupId())
    //                 .orElseThrow(() -> new RuntimeException("Group not found"));
    //     }

    //     // Resolve splitAmong from usernames
    //     List<User> splitAmong = new ArrayList<>();
    //     if (dto.getSplitAmongUsernames() != null && !dto.getSplitAmongUsernames().isEmpty()) {
    //         splitAmong = userRepository.findByUsernameIn(dto.getSplitAmongUsernames());
    //         if (splitAmong.isEmpty()) {
    //             throw new RuntimeException("No valid users found in splitAmong");
    //         }
    //     } else if (group != null) {
    //         // Default: all group members
    //         splitAmong = new ArrayList<>(group.getMembers());
    //     } else {
    //         // Personal expense: only paidBy
    //         splitAmong = List.of(paidBy);
    //     }

    //     // Create and save expense
    //     Expense expense = new Expense();
    //     expense.setAmount(dto.getAmount());
    //     expense.setDescription(dto.getDescription());
    //     expense.setDate(dto.getDate());
    //     expense.setCategory(categoryRepository.findByName(dto.getCategoryName())
    //             .orElseGet(() -> {
    //                 Category c = new Category();
    //                 c.setName(dto.getCategoryName());
    //                 return categoryRepository.save(c);
    //             }));
    //     expense.setPaidBy(paidBy);
    //     expense.setGroup(group);
    //     expense.setSplitAmong(new HashSet<>(splitAmong));

    //     Expense saved = expenseRepository.save(expense);

    //     // ✅ Generate splits only if group expense
    //     if (group != null) {
    //         groupService.createSplitsForExpense(saved, splitAmong);
    //     }

    //     return ResponseEntity.ok(new ExpenseDTO(saved));
    // }


    // @GetMapping
    // public ResponseEntity<List<ExpenseDTO>> getAllExpenses() {
    //     return ResponseEntity.ok(
    //     expenseService.getAllExpenses().stream()
    //         .map(ExpenseDTO::new)
    //         .collect(Collectors.toList())
    // );
    // }
   

    @PostMapping("/expenses")
    public ResponseEntity<ExpenseDTO> addExpense(@RequestBody ExpenseDTO dto) {
        ExpenseDTO saved = expenseService.addExpense(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<ExpenseDTO>> getAllExpenses() {
        List<ExpenseDTO> expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(expenses);
    }
    
}

