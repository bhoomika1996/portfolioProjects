package com.bhoomika.ExpenseTrackerApp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhoomika.ExpenseTrackerApp.entity.Expense;
import com.bhoomika.ExpenseTrackerApp.entity.ExpenseGroup;
import com.bhoomika.ExpenseTrackerApp.entity.Split;
import com.bhoomika.ExpenseTrackerApp.entity.User;
import com.bhoomika.ExpenseTrackerApp.repository.ExpenseGroupRepository;
import com.bhoomika.ExpenseTrackerApp.repository.ExpenseRepository;
import com.bhoomika.ExpenseTrackerApp.repository.SplitRepository;

@Service
public class ExpenseGroupService {
    @Autowired private ExpenseGroupRepository groupRepo;
    @Autowired private ExpenseRepository expenseRepo;
    @Autowired private SplitRepository splitRepo;

    // Create a new group
    public ExpenseGroup createGroup(String name, Set<User> members) {
        ExpenseGroup group = new ExpenseGroup();
        group.setName(name);
        group.setMembers(members);
        return groupRepo.save(group);
    }

    // Add expense to group and generate splits
    public Expense addGroupExpense(Long groupId, Expense expense, List<User> splitAmong) {
        ExpenseGroup group = groupRepo.findById(groupId)
            .orElseThrow(() -> new RuntimeException("Group not found"));

        expense.setGroup(group);
        expense.setSplitAmong(splitAmong);

        Expense savedExpense = expenseRepo.save(expense);

        // Generate splits: each participant owes share to payer
        double share = expense.getAmount() / splitAmong.size();
        for (User member : splitAmong) {
            if (!member.equals(expense.getPaidBy())) {
                Split split = new Split();
                split.setExpense(savedExpense);
                split.setOwedBy(member);
                split.setOwedTo(expense.getPaidBy());
                split.setAmount(share);
                split.setSettled(false);
                splitRepo.save(split);
            }
        }

        return savedExpense;
    }

    // Get net balances for all users in group
    public Map<User, Double> getGroupBalances(Long groupId) {
        List<Split> splits = splitRepo.findByExpense_Group_IdAndSettledFalse(groupId);
        Map<User, Double> net = new HashMap<>();

        for (Split split : splits) {
            net.put(split.getOwedBy(), net.getOrDefault(split.getOwedBy(), 0.0) - split.getAmount());
            net.put(split.getOwedTo(), net.getOrDefault(split.getOwedTo(), 0.0) + split.getAmount());
        }

        return net;
    }

    public ExpenseGroup saveGroup(ExpenseGroup group) {
        return groupRepo.save(group);
    }
    public ExpenseGroup findGroupById(Long id) {
        return groupRepo.findById(id).orElse(null);
    }

    public void createSplitsForExpense(Expense expense, List<User> splitAmong) {
        double share = expense.getAmount() / splitAmong.size();
        for (User member : splitAmong) {
            if (!member.equals(expense.getPaidBy())) {
                Split split = new Split();
                split.setExpense(expense);
                split.setOwedBy(member);
                split.setOwedTo(expense.getPaidBy());
                split.setAmount(share);
                split.setSettled(false);
                splitRepo.save(split);
            }
        }
    }
    
}



