// BalanceService.java_v1
// Service to calculate and update balances based on expenses

package com.bhoomika.ExpenseTracker.service;

import com.bhoomika.ExpenseTracker.dto.BalanceResponse;
import com.bhoomika.ExpenseTracker.model.*;
import com.bhoomika.ExpenseTracker.repository.BalanceRepository;
import com.bhoomika.ExpenseTracker.repository.ExpenseParticipantRepository;
import com.bhoomika.ExpenseTracker.repository.MembersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BalanceService {

    @Autowired
    private ExpenseParticipantRepository expenseParticipantRepository;

    @Autowired
    private MembersRepository membersRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    /**
     * Calculate net balances for all user pairs in a group
     * Returns list of who owes whom
     */
    @Transactional
    public List<BalanceResponse> calculateBalances(Long groupId) {
        // Clear existing balances (we'll recalculate)
        balanceRepository.clearBalancesForGroup(groupId);

        // Get all members in the group
        List<Long> memberIds = membersRepository.findByGroupId(groupId)
                .stream()
                .map(Members::getUserId)
                .collect(Collectors.toList());

        // Map: (from, to) -> net amount owed
        Map<String, Double> netBalances = new HashMap<>();

        // For each member, calculate how much they paid and are owed
        Map<Long, Double> totalPaid = new HashMap<>(); // user -> total amount they paid
        Map<Long, Double> totalOwed = new HashMap<>(); // user -> total amount others owe them

        // Initialize
        memberIds.forEach(id -> {
            totalPaid.put(id, 0.0);
            totalOwed.put(id, 0.0);
        });

        // Sum up all expenses
        expenseParticipantRepository.findByExpenseGroupGroupId(groupId)
                .forEach(ep -> {
                    Long payer = ep.getExpense().getPaidBy().getUserId();
                    Long participant = ep.getParticipant().getUserId();
                    Double share = ep.getShareAmount();

                    // Payer paid the full amount, so they are owed
                    totalPaid.merge(payer, share, Double::sum);

                    // Participant owes their share
                    if (!payer.equals(participant)) {
                        totalOwed.merge(participant, share, Double::sum);
                    }
                });

        // Calculate net: who owes whom
        for (Long from : memberIds) {
            for (Long to : memberIds) {
                if (from.equals(to)) continue;

                Double paidByFrom = totalPaid.getOrDefault(from, 0.0);
                Double owedByFrom = totalOwed.getOrDefault(from, 0.0);

                Double net = paidByFrom - owedByFrom;

                if (net > 0) {
                    // 'from' is owed money → 'to' owes 'from'
                    String key = to + "->" + from;
                    netBalances.merge(key, net, Double::sum);
                }
            }
        }

        // Save to DB and build response
        List<BalanceResponse> responses = new ArrayList<>();
        for (Map.Entry<String, Double> entry : netBalances.entrySet()) {
            String[] parts = entry.getKey().split("->");
            Long to = Long.parseLong(parts[0]);
            Long from = Long.parseLong(parts[1]);
            Double amount = entry.getValue();

            Balance balance = new Balance();
            balance.setFromUser(new User() {{ setUserId(from); }});
            balance.setToUser(new User() {{ setUserId(to); }});
            balance.setAmount(amount);
            balance.setGroup(new Group() {{ setGroupId(groupId); }});

            balanceRepository.save(balance);

            BalanceResponse res = new BalanceResponse();
            res.setFromUserId(from);
            res.setToUserId(to);
            res.setAmount(amount);
            responses.add(res);
        }

        return responses;
    }

    /**
     * Get balance between two users
     */
    public Double getBalanceBetween(Long groupId, Long fromId, Long toId) {
        Balance balance = balanceRepository.findByGroupAndUsers(groupId, fromId, toId);
        return balance != null ? balance.getAmount() : 0.0;
    }
    
}
