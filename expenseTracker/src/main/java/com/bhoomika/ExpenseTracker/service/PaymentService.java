// PaymentService.java_v2
// Service to record and retrieve payments

package com.bhoomika.ExpenseTracker.service;

import com.bhoomika.ExpenseTracker.dto.PaymentRequest;
import com.bhoomika.ExpenseTracker.dto.PaymentResponse;
import com.bhoomika.ExpenseTracker.model.*;
import com.bhoomika.ExpenseTracker.repository.PaymentRepository;
import com.bhoomika.ExpenseTracker.repository.UserRepository;

import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;

import com.bhoomika.ExpenseTracker.repository.GroupRepository;
import com.bhoomika.ExpenseTracker.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bhoomika.ExpenseTracker.repository.TransactionLogRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    private final Object paymentLock = new Object();

    @Transactional
    public PaymentResponse recordPayment(PaymentRequest request) {
        // Synchronize to ensure atomic update of balances and payment record----------not required with optimistic locking
        //Optimistic locking ensures thread safety.
        synchronized (paymentLock) {
            User fromUser = userRepository.findById(request.getFromUserId())
                    .orElseThrow(() -> new RuntimeException("From user not found"));
            User toUser = userRepository.findById(request.getToUserId())
                    .orElseThrow(() -> new RuntimeException("To user not found"));
            Group group = groupRepository.findById(request.getGroupId())
                    .orElseThrow(() -> new RuntimeException("Group not found"));

            Payment payment = new Payment();
            payment.setFromUser(fromUser);
            payment.setToUser(toUser);
            payment.setGroup(group);
            payment.setAmount(request.getAmount());
            payment.setTimestamp(LocalDateTime.now());

            Payment savedPayment = paymentRepository.save(payment);

            // // Retry updating balances up to 3 times if concurrent modification detected
            // int retries = 3;
            // while (retries > 0) {
            //     try {
                    updateBalancesAfterPayment(group, fromUser, toUser, request.getAmount());
                //     break; // success; exit retry loop
                // } catch (OptimisticLockException e) {
                //     retries--;
                //     if (retries == 0) {
                //         throw e; // rethrow after max retries
                //     }
                //     // Optional small delay before retrying:
                //     // Thread.sleep(100);
                // }
            
            logTransaction(group, fromUser, toUser, request.getAmount(), "Payment settlement");
            return mapToResponse(savedPayment);
        }
    }

    private void updateBalancesAfterPayment(Group group, User fromUser, User toUser, Double amount) {
        // Get balance: fromUser owes toUser
        Balance fromToBalance = balanceRepository.findByGroupAndUsers(group.getGroupId(),
                fromUser.getUserId(), toUser.getUserId());

        if (fromToBalance == null) {
            fromToBalance = new Balance();
            fromToBalance.setGroup(group);
            fromToBalance.setFromUser(fromUser);
            fromToBalance.setToUser(toUser);
            fromToBalance.setAmount(0.0);
        }

        // Get reverse balance: toUser owes fromUser
        Balance toFromBalance = balanceRepository.findByGroupAndUsers(group.getGroupId(),
                toUser.getUserId(), fromUser.getUserId());

        if (toFromBalance == null) {
            toFromBalance = new Balance();
            toFromBalance.setGroup(group);
            toFromBalance.setFromUser(toUser);
            toFromBalance.setToUser(fromUser);
            toFromBalance.setAmount(0.0);
        }

        // Adjust balances according to payment amount
        double newFromToAmt = fromToBalance.getAmount() - amount;

        if (newFromToAmt < 0) {
            // Now 'fromUser' owes 'toUser' net positive balance
            toFromBalance.setAmount(Math.abs(newFromToAmt));
            balanceRepository.save(toFromBalance);

            fromToBalance.setAmount(0.0);
            balanceRepository.save(fromToBalance);
        } else {
            // 'toUser' owes 'fromUser' reduced amount or zero
            fromToBalance.setAmount(newFromToAmt);
            balanceRepository.save(fromToBalance);

            toFromBalance.setAmount(0.0);
            balanceRepository.save(toFromBalance);
        }
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

    public List<PaymentResponse> getPaymentsByGroup(Long groupId) {
        return paymentRepository.findByGroupId(groupId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PaymentResponse> getPaymentsByUser(Long userId) {
        List<Payment> payments = paymentRepository.findByFromUserUserId(userId);
        payments.addAll(paymentRepository.findByToUserUserId(userId));
        // Deduplicate if needed
        return payments.stream()
                .distinct()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PaymentResponse mapToResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(payment.getPaymentId());
        response.setFromUserId(payment.getFromUser().getUserId());
        response.setToUserId(payment.getToUser().getUserId());
        response.setGroupId(payment.getGroup().getGroupId());
        response.setAmount(payment.getAmount());
        response.setTimestamp(payment.getTimestamp());
        return response;
    }
}
