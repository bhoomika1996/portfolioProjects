package com.bhoomika.ExpenseTrackerApp.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhoomika.ExpenseTrackerApp.entity.Expense;
import com.bhoomika.ExpenseTrackerApp.entity.ExpenseGroup;
import com.bhoomika.ExpenseTrackerApp.entity.Split;
import com.bhoomika.ExpenseTrackerApp.entity.User;
import com.bhoomika.ExpenseTrackerApp.repository.ExpenseGroupRepository;
import com.bhoomika.ExpenseTrackerApp.repository.ExpenseRepository;
import com.bhoomika.ExpenseTrackerApp.repository.SplitRepository;
import com.bhoomika.ExpenseTrackerApp.repository.UserRepository;
import com.bhoomika.ExpenseTrackerApp.service.ExpenseGroupService;

@RestController
@RequestMapping("/api/groups")
public class ExpenseGroupController {
     @Autowired
    private ExpenseGroupService groupService;
    @Autowired
    private ExpenseGroupRepository expenseGroupRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired private SplitRepository splitRepository;

    // DTO for group creation (frontend sends: {name, memberIds: [Long]})
    public static class CreateGroupDto {
        public String name;
        public List<Long> memberIds;
    }
    // DTO
    public static class SettleDto {
        public Long owedById;
        public Long owedToId;
    }


    // DTO for adding expense (frontend sends: {description, amount, date, paidById, splitAmongIds})
    public static class AddExpenseDto {
        public String description;
        public Double amount;
        public String date; // frontend sends as String (e.g. "2025-10-10")
        public Long paidById;
        public List<Long> splitAmongIds;
    }

    // Create a group
    @PostMapping
    public ResponseEntity<ExpenseGroup> createGroup(@RequestBody CreateGroupDto dto) {
        Set<User> members = new HashSet<>(userRepository.findAllById(dto.memberIds));
        if (members.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        ExpenseGroup group = new ExpenseGroup();
        group.setName(dto.name);
        group.setMembers(members);
        ExpenseGroup savedGroup = groupService.saveGroup(group);
        return ResponseEntity.ok(savedGroup);
    }

    // Add expense to group
    @PostMapping("/{groupId}/expenses")
    public ResponseEntity<Expense> addGroupExpense(
            @PathVariable Long groupId,
            @RequestBody AddExpenseDto dto) {

                ExpenseGroup group = expenseGroupRepository.findById(groupId).orElse(null);
                if (group == null) return ResponseEntity.badRequest().build();

                User paidBy = userRepository.findById(dto.paidById).orElse(null);
                if (paidBy == null) return ResponseEntity.badRequest().build();

        List<User> splitAmong = userRepository.findAllById(dto.splitAmongIds);

        if (group == null || paidBy == null || splitAmong.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Expense expense = new Expense();
        expense.setDescription(dto.description);
        expense.setAmount(dto.amount);
        expense.setDate(LocalDate.parse(dto.date)); // parse date from String
        expense.setPaidBy(paidBy);
        expense.setGroup(group);
        expense.setSplitAmong(splitAmong);

        Expense savedExpense = expenseRepository.save(expense);
        // Optionally, call service to auto-generate splits for this expense

        return ResponseEntity.ok(savedExpense);
    }

    // Get groups for user (for dropdowns, etc.)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExpenseGroup>> getGroupsForUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new ArrayList<>(user.getGroups()));
    }

    @GetMapping("/{groupId}/balances")
    public ResponseEntity<Map<String, Double>> getGroupBalances(@PathVariable Long groupId) {
        Map<User, Double> rawBalances = groupService.getGroupBalances(groupId);
        Map<String, Double> balances = new HashMap<>();
        for (Map.Entry<User, Double> entry : rawBalances.entrySet()) {
            balances.put(entry.getKey().getUsername(), entry.getValue());
        }
        return ResponseEntity.ok(balances);
    }

    @GetMapping("/{groupId}/expenses")
    public ResponseEntity<List<Expense>> getGroupExpenses(@PathVariable Long groupId) {
        List<Expense> expenses = expenseRepository.findByGroup_Id(groupId);
        return ResponseEntity.ok(expenses);
    }

    @PostMapping("/{groupId}/settle")
    public ResponseEntity<?> settleUp(@PathVariable Long groupId, @RequestBody SettleDto dto) {
        List<Split> splits = splitRepository.findByExpense_Group_IdAndSettledFalse(groupId);
        for (Split split : splits) {
            if (split.getOwedBy().getId().equals(dto.owedById) &&
                split.getOwedTo().getId().equals(dto.owedToId)) {
                split.setSettled(true);
                splitRepository.save(split);
            }
        }
        return ResponseEntity.ok().build();
    }

}

