// BalanceController.java_v1
// REST API for fetching balance summaries in a group
// Shows who owes whom and how much

package com.bhoomika.ExpenseTracker.contoller;

import com.bhoomika.ExpenseTracker.dto.BalanceResponse;
import com.bhoomika.ExpenseTracker.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/balances")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    /**
     * Get all balances in a group
     * GET /api/balances/group/{groupId}
     */
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<BalanceResponse>> getBalancesByGroup(@PathVariable Long groupId) {
        List<BalanceResponse> balances = balanceService.calculateBalances(groupId);
        return ResponseEntity.ok(balances);
    }

    /**
     * Get net balance between two users in a group
     * GET /api/balances/group/{groupId}/between?from=1&to=2
     */
    @GetMapping("/group/{groupId}/between")
    public ResponseEntity<Double> getBalanceBetween(
            @PathVariable Long groupId,
            @RequestParam Long from,
            @RequestParam Long to) {
        Double balance = balanceService.getBalanceBetween(groupId, from, to);
        return ResponseEntity.ok(balance);
    }
}
