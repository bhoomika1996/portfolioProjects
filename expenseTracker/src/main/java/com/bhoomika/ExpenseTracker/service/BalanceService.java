// BalanceService.java_v2
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
import com.bhoomika.ExpenseTracker.repository.TransactionLogRepository;

import jakarta.persistence.OptimisticLockException;

import java.time.LocalDateTime;
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

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    @Transactional
    public void updateBalancesAndLogTransactionAfterExpense(Expense expense, List<ExpenseParticipant> participants) {
        // int attempts = 0;
        // while (attempts < 3){
        //     try {
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

                    // Log transaction for audit trail
                    logTransaction(expense.getGroup(), ep.getParticipant(), expense.getPaidBy(), shareAmount,
                        "Expense split for expense ID: " + expense.getExpenseId());
                    // break;
                }
            // }catch (OptimisticLockException e) {
            //         attempts++;
            //         if (attempts == 3)
            //             throw e;
            //         // Optionally Thread.sleep(...) before retry
            // }
        // }   
    }

    private void logTransaction(Group group, User fromUser, User toUser, Double amount, String description) {
        TransactionLog log = new TransactionLog();
        log.setTimestamp(LocalDateTime.now());
        log.setFromUser(fromUser);
        log.setToUser(toUser);
        log.setAmount(amount);
        log.setGroup(group);
        log.setDescription(description);
        transactionLogRepository.save(log);
    }
    /**
     * Simply fetch balances from database — no recalculation here
     */
    public List<BalanceResponse> getBalancesForGroup(Long groupId) {
        List<Balance> balances = balanceRepository.findByGroupId(groupId);
        return balances.stream().map(b -> {
            BalanceResponse response = new BalanceResponse();
            response.setFromUserId(b.getFromUser().getUserId());
            response.setToUserId(b.getToUser().getUserId());
            response.setAmount(b.getAmount());
            return response;
        }).collect(Collectors.toList());
    }

    /**
     * Get balance between two users
     */
    public Double getBalanceBetween(Long groupId, Long fromId, Long toId) {
        Balance balance = balanceRepository.findByGroupAndUsers(groupId, fromId, toId);
        return balance != null ? balance.getAmount() : 0.0;
    }

    @Transactional
    public List<BalanceResponse> simplifyDebts(Long groupId) {
        // Step 1: Fetch all current balances
        List<Balance> allBalances = balanceRepository.findByGroupId(groupId);

        // Step 2: Aggregate net amounts per user
        // userId -> net amount owed (+ve means user is owed money, -ve means user owes)
        Map<Long, Double> netAmounts = new HashMap<>();

        for (Balance balance : allBalances) {
            Long fromUser = balance.getFromUser().getUserId();
            Long toUser = balance.getToUser().getUserId();
            double amount = balance.getAmount();

            netAmounts.put(fromUser, netAmounts.getOrDefault(fromUser, 0.0) - amount);
            netAmounts.put(toUser, netAmounts.getOrDefault(toUser, 0.0) + amount);
        }

        // Step 3: Lists of debtors and creditors
        PriorityQueue<UserAmount> debtors = new PriorityQueue<>((a, b) -> Double.compare(a.amount, b.amount)); // negative amounts
        PriorityQueue<UserAmount> creditors = new PriorityQueue<>((a, b) -> Double.compare(b.amount, a.amount)); // positive amounts

        for (Map.Entry<Long, Double> entry : netAmounts.entrySet()) {
            double amt = entry.getValue();
            if (Math.abs(amt) < 0.01) continue; // ignore near zero
            if (amt < 0)
                debtors.offer(new UserAmount(entry.getKey(), amt));
            else
                creditors.offer(new UserAmount(entry.getKey(), amt));
        }

        // Step 4: Clear previous balances
        balanceRepository.clearBalancesForGroup(groupId);

        List<BalanceResponse> simplifiedBalances = new ArrayList<>();

        // Step 5: Simplify debts greedily
        while (!debtors.isEmpty() && !creditors.isEmpty()) {
            UserAmount debtor = debtors.poll();
            UserAmount creditor = creditors.poll();

            double settleAmount = Math.min(-debtor.amount, creditor.amount);

            Balance newBalance = new Balance();
            newBalance.setGroup(allBalances.get(0).getGroup());
            // From debtor to creditor
            // Debtor owes creditor settleAmount
            newBalance.setFromUser(new com.bhoomika.ExpenseTracker.model.User() {{ setUserId(debtor.userId); }});
            newBalance.setToUser(new com.bhoomika.ExpenseTracker.model.User() {{ setUserId(creditor.userId); }});
            newBalance.setAmount(settleAmount);
            balanceRepository.save(newBalance);

            simplifiedBalances.add(convertToResponse(newBalance));

            // Update amounts
            debtor.amount += settleAmount;
            creditor.amount -= settleAmount;

            if (Math.abs(debtor.amount) > 0.01) debtors.offer(debtor);
            if (Math.abs(creditor.amount) > 0.01) creditors.offer(creditor);
        }

        return simplifiedBalances;
    }

    private BalanceResponse convertToResponse(Balance balance) {
        BalanceResponse resp = new BalanceResponse();
        resp.setFromUserId(balance.getFromUser().getUserId());
        resp.setToUserId(balance.getToUser().getUserId());
        resp.setAmount(balance.getAmount());
        return resp;
    }

    private static class UserAmount {
        Long userId;
        double amount;
        UserAmount(Long userId, double amount) {
            this.userId = userId;
            this.amount = amount;
        }
    }

}
