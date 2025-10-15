// PaymentService.java_v1
// Service to record and retrieve payments

package com.bhoomika.ExpenseTracker.service;

import com.bhoomika.ExpenseTracker.dto.PaymentRequest;
import com.bhoomika.ExpenseTracker.dto.PaymentResponse;
import com.bhoomika.ExpenseTracker.model.*;
import com.bhoomika.ExpenseTracker.repository.PaymentRepository;
import com.bhoomika.ExpenseTracker.repository.UserRepository;
import com.bhoomika.ExpenseTracker.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public PaymentResponse recordPayment(PaymentRequest request) {
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

        return mapToResponse(savedPayment);
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
