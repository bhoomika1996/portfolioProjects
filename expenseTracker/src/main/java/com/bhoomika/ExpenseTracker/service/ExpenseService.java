// ExpenseService.java_v1
// Business logic for expense creation and split calculation

package com.bhoomika.ExpenseTracker.service;

import com.bhoomika.ExpenseTracker.dto.*;
import com.bhoomika.ExpenseTracker.model.*;
import com.bhoomika.ExpenseTracker.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseParticipantRepository expenseParticipantRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MembersRepository membersRepository;

    public ExpenseResponse createExpense(ExpenseRequest request) {
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User paidBy = userRepository.findById(request.getPaidBy())
                .orElseThrow(() -> new RuntimeException("Payer not found"));

        // Validate participants are members of the group
        List<Long> memberIds = membersRepository.findByGroupId(request.getGroupId())
                .stream()
                .map(Members::getUserId)
                .collect(Collectors.toList());

        Map<Long, Double> shareMap = calculateShares(request, memberIds);

        Expense expense = new Expense();
        expense.setTitle(request.getTitle());
        expense.setTotalAmount(request.getTotalAmount());
        expense.setCurrency(request.getCurrency());
        expense.setGroup(group);
        expense.setPaidBy(paidBy);
        expense.setDate(LocalDateTime.now());

        Expense savedExpense = expenseRepository.save(expense);

        List<ExpenseParticipant> participants = new ArrayList<>();
        for (Map.Entry<Long, Double> entry : shareMap.entrySet()) {
            User user = userRepository.findById(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("User not found: " + entry.getKey()));

            ExpenseParticipant participant = new ExpenseParticipant();
            participant.setExpense(savedExpense);
            participant.setParticipant(user);
            participant.setShareAmount(entry.getValue());
            participant.setSplitType(request.getSplitType());
            participants.add(participant);
        }

        expenseParticipantRepository.saveAll(participants);

        // Update balances immediately after expense creation
        updateBalancesAfterExpense(savedExpense,participants);

        return mapToResponse(savedExpense, participants);
    }

    /**
     * Incrementally update balances for the group after an expense is added.
     */
    private void updateBalancesAfterExpense(Expense expense, List<ExpenseParticipant> participants) {
        Long groupId = expense.getGroup().getGroupId();

        // For the payer, increase total paid by full expense amount
        Long payerId = expense.getPaidBy().getUserId();

        // For each participant calculate what they owe
        for (ExpenseParticipant ep : participants) {
            Long participantId = ep.getParticipant().getUserId();

            // Skip updating balance if participant == payer — no debt to self
            if (participantId.equals(payerId)) continue;

            Double shareAmount = ep.getShareAmount();

            // Adjust balances: participant owes payer

            // Fetch existing balances (participant owes payer)
            Balance participantToPayer = balanceRepository.findByGroupAndUsers(groupId, participantId, payerId);
            if (participantToPayer == null) {
                participantToPayer = new Balance();
                participantToPayer.setGroup(expense.getGroup());
                participantToPayer.setFromUser(ep.getParticipant());
                participantToPayer.setToUser(expense.getPaidBy());
                participantToPayer.setAmount(0.0);
            }

            // Fetch reverse balance (payer owes participant), if any
            Balance payerToParticipant = balanceRepository.findByGroupAndUsers(groupId, payerId, participantId);
            if (payerToParticipant == null) {
                payerToParticipant = new Balance();
                payerToParticipant.setGroup(expense.getGroup());
                payerToParticipant.setFromUser(expense.getPaidBy());
                payerToParticipant.setToUser(ep.getParticipant());
                payerToParticipant.setAmount(0.0);
            }

            // Calculate net balance update logic:
            // If payer to participant has amount X, participant owes payer amount Y

            Double newPayerToParticipantAmt = payerToParticipant.getAmount() - shareAmount;

            if (newPayerToParticipantAmt < 0) {
                // Participant owes payer (net positive)
                participantToPayer.setAmount(Math.abs(newPayerToParticipantAmt));
                balanceRepository.save(participantToPayer);

                // Reverse balance is 0 or cleared
                payerToParticipant.setAmount(0.0);
                balanceRepository.save(payerToParticipant);
            } else {
                // Payer still owes participant some or equals zero
                payerToParticipant.setAmount(newPayerToParticipantAmt);
                balanceRepository.save(payerToParticipant);

                participantToPayer.setAmount(0.0);
                balanceRepository.save(participantToPayer);
            }
        }
    }

    private Map<Long, Double> calculateShares(ExpenseRequest request, List<Long> memberIds) {
        Map<Long, Double> shares = new HashMap<>();

        switch (request.getSplitType()) {
            case EQUAL:
                double equalShare = request.getTotalAmount() / memberIds.size();
                memberIds.forEach(id -> shares.put(id, equalShare));
                break;

            case EXACT:
                if (request.getExactAmounts() == null) {
                    throw new RuntimeException("Exact amounts required for EXACT split");
                }
                shares.putAll(request.getExactAmounts());
                break;

            case PERCENTAGE:
                if (request.getPercentages() == null) {
                    throw new RuntimeException("Percentages required for PERCENTAGE split");
                }
                double totalPercentage = request.getPercentages().values().stream().mapToDouble(Double::doubleValue).sum();
                if (Math.abs(totalPercentage - 100.0) > 0.01) {
                    throw new RuntimeException("Percentages must sum to 100%");
                }
                request.getPercentages().forEach((userId, pct) ->
                    shares.put(userId, request.getTotalAmount() * pct / 100.0)
                );
                break;
        }

        return shares;
    }

    public List<ExpenseResponse> getExpensesByGroup(Long groupId) {
        List<Expense> expenses = expenseRepository.findByGroupId(groupId);
        return expenses.stream()
                .map(this::mapToResponseWithParticipants)
                .collect(Collectors.toList());
    }

    public List<ExpenseResponse> getExpensesByUser(Long userId) {
        List<Expense> expenses = expenseRepository.findByParticipantId(userId);
        return expenses.stream()
                .map(this::mapToResponseWithParticipants)
                .collect(Collectors.toList());
    }

    private ExpenseResponse mapToResponseWithParticipants(Expense expense) {
        List<ExpenseParticipant> participants = expenseParticipantRepository
                .findByExpenseExpenseId(expense.getExpenseId());
        return mapToResponse(expense, participants);
    }

    private ExpenseResponse mapToResponse(Expense expense, List<ExpenseParticipant> participants) {
        ExpenseResponse response = new ExpenseResponse();
        response.setExpenseId(expense.getExpenseId());
        response.setTitle(expense.getTitle());
        response.setTotalAmount(expense.getTotalAmount());
        response.setCurrency(expense.getCurrency());
        response.setGroupId(expense.getGroup().getGroupId());
        response.setPaidBy(expense.getPaidBy().getUserId());
        response.setDate(expense.getDate());

        List<ExpenseParticipantResponse> participantResponses = participants.stream()
                .map(p -> {
                    ExpenseParticipantResponse pr = new ExpenseParticipantResponse();
                    pr.setUserId(p.getParticipant().getUserId());
                    pr.setShareAmount(p.getShareAmount());
                    pr.setSplitType(p.getSplitType().name());
                    return pr;
                })
                .collect(Collectors.toList());

        response.setParticipants(participantResponses);
        return response;
    }
}
