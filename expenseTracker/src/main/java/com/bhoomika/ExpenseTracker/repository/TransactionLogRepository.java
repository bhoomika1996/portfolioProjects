package com.bhoomika.ExpenseTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bhoomika.ExpenseTracker.model.TransactionLog;
@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
    
}
