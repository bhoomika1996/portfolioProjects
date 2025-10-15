package com.bhoomika.ExpenseTracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bhoomika.ExpenseTracker.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.group.groupId = :groupId")
    List<Payment> findByGroupId(Long groupId);
    List<Payment> findByFromUserUserId(Long userId);
    List<Payment> findByToUserUserId(Long userId);
}
