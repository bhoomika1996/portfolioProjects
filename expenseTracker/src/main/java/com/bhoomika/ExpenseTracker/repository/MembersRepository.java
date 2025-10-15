package com.bhoomika.ExpenseTracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bhoomika.ExpenseTracker.model.MemberId;
import com.bhoomika.ExpenseTracker.model.Members;

public interface MembersRepository extends JpaRepository<Members, MemberId> {
    List<Members> findByGroupId(Long groupId);
    List<Members> findByUserId(Long userId);
    boolean existsByGroupIdAndUserId(Long groupId, Long userId);
}
