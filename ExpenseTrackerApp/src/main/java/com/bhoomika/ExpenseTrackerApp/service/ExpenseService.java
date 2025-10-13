package com.bhoomika.ExpenseTrackerApp.service;

import com.bhoomika.ExpenseTrackerApp.dto.ExpenseDTO;
import com.bhoomika.ExpenseTrackerApp.entity.Category;
import com.bhoomika.ExpenseTrackerApp.entity.Expense;
import com.bhoomika.ExpenseTrackerApp.entity.ExpenseGroup;
import com.bhoomika.ExpenseTrackerApp.entity.Split;
import com.bhoomika.ExpenseTrackerApp.entity.User;
import com.bhoomika.ExpenseTrackerApp.repository.CategoryRepository;
import com.bhoomika.ExpenseTrackerApp.repository.ExpenseGroupRepository;
import com.bhoomika.ExpenseTrackerApp.repository.ExpenseRepository;
import com.bhoomika.ExpenseTrackerApp.repository.SplitRepository;
import com.bhoomika.ExpenseTrackerApp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired private ExpenseRepository expenseRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ExpenseGroupRepository groupRepository;
    @Autowired private SplitRepository splitRepository;

    public ExpenseDTO addExpense(ExpenseDTO dto) {
        User paidBy = userRepository.findById(dto.getPaidById())
                .orElseThrow(() -> new RuntimeException("Payer not found"));

        ExpenseGroup group = null;
        if (dto.getGroupId() != null) {
            group = groupRepository.findById(dto.getGroupId())
                    .orElseThrow(() -> new RuntimeException("Group not found"));
        }

        Category category = categoryRepository.findByName(dto.getCategoryName())
                .orElseGet(() -> {
                    Category c = new Category();
                    c.setName(dto.getCategoryName());
                    return categoryRepository.save(c);
                });

        List<User> splitAmong = resolveSplitAmong(dto, group, paidBy);

        Expense expense = new Expense();
        expense.setAmount(dto.getAmount());
        expense.setDescription(dto.getDescription());
        expense.setDate(dto.getDate());
        expense.setPaidBy(paidBy);
        expense.setGroup(group);
        expense.setCategory(category);
        expense.setSplitAmong(new HashSet<>(splitAmong));

        Expense saved = expenseRepository.save(expense);

        if (group != null) {
            createSplits(saved, splitAmong);
        }

        return new ExpenseDTO(saved);
    }

    private List<User> resolveSplitAmong(ExpenseDTO dto, ExpenseGroup group, User paidBy) {
        if (dto.getSplitAmongUsernames() != null && !dto.getSplitAmongUsernames().isEmpty()) {
            List<User> users = userRepository.findByUsernameIn(dto.getSplitAmongUsernames());
            if (users.isEmpty()) throw new RuntimeException("No valid users found");
            return users;
        } else if (group != null) {
            return new ArrayList<>(group.getMembers());
        } else {
            return List.of(paidBy);
        }
    }

    private void createSplits(Expense expense, List<User> splitAmong) {
        double share = expense.getAmount() / splitAmong.size();
        for (User user : splitAmong) {
            if (!user.equals(expense.getPaidBy())) {
                Split split = new Split();
                split.setExpense(expense);
                split.setOwedBy(user);
                split.setOwedTo(expense.getPaidBy());
                split.setAmount(share);
                split.setSettled(false);
                splitRepository.save(split);
            }
        }
    }

    public List<ExpenseDTO> getAllExpenses() {
        return expenseRepository.findAll().stream()
                .map(ExpenseDTO::new)
                .collect(Collectors.toList());
    }
   
    // public List<Expense> getExpensesByUserId(Long userId) {
    //     return expenseRepository.findByPaidById(userId);
    // }

    // public List<Expense> getExpensesByGroupId(Long groupId) {
    //     return expenseRepository.findByGroup_Id(groupId);
    // }
}

