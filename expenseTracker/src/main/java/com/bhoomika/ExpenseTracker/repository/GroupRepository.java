package com.bhoomika.ExpenseTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bhoomika.ExpenseTracker.model.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {}
