// PaymentController.java_v1
// REST API for recording payments (settle up)
// Handles marking debts as paid

package com.bhoomika.ExpenseTracker.contoller;

import com.bhoomika.ExpenseTracker.dto.PaymentRequest;
import com.bhoomika.ExpenseTracker.dto.PaymentResponse;
import com.bhoomika.ExpenseTracker.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * Record a payment between two users
     * POST /api/payments
     * Request Body: { "fromUserId": 1, "toUserId": 2, "groupId": 1, "amount": 500.0 }
     */
    @PostMapping
    public ResponseEntity<PaymentResponse> recordPayment(@RequestBody PaymentRequest request) {
        PaymentResponse payment = paymentService.recordPayment(request);
        return ResponseEntity.ok(payment);
    }

    /**
     * Get all payments in a group
     * GET /api/payments/group/{groupId}
     */
    @GetMapping("/group/{groupId}")
    public ResponseEntity<java.util.List<PaymentResponse>> getPaymentsByGroup(@PathVariable Long groupId) {
        java.util.List<PaymentResponse> payments = paymentService.getPaymentsByGroup(groupId);
        return ResponseEntity.ok(payments);
    }

    /**
     * Get all payments made by a user
     * GET /api/payments/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<java.util.List<PaymentResponse>> getPaymentsByUser(@PathVariable Long userId) {
        java.util.List<PaymentResponse> payments = paymentService.getPaymentsByUser(userId);
        return ResponseEntity.ok(payments);
    }
}
