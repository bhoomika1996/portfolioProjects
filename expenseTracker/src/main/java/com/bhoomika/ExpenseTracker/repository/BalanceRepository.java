package com.bhoomika.ExpenseTracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bhoomika.ExpenseTracker.model.Balance;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    // Find all balances in a group
    @Query("SELECT b FROM Balance b WHERE b.group.groupId = :groupId")
    List<Balance> findByGroupId(@Param("groupId") Long groupId);

    // Find balance from user A to user B in a group
    @Query("SELECT b FROM Balance b WHERE b.group.groupId = :groupId " +
            "AND b.fromUser.userId = :fromId AND b.toUser.userId = :toId")
    Balance findByGroupAndUsers(@Param("groupId") Long groupId,
                                @Param("fromId") Long fromId,
                                @Param("toId") Long toId);

    // Optional: Delete all balances for a group (useful for recalculation)
    //void deleteByGroupGroupId(Long groupId);
    @Modifying
    @Query("DELETE FROM Balance b WHERE b.group.groupId = :groupId")
    void clearBalancesForGroup(@Param("groupId") Long groupId);
}
