// GroupController.java_v1
// REST API for managing groups (create, get, add members)
// Handles HTTP requests and delegates business logic to GroupService

package com.bhoomika.ExpenseTracker.contoller;

import com.bhoomika.ExpenseTracker.dto.GroupRequest;
import com.bhoomika.ExpenseTracker.dto.GroupResponse;
import com.bhoomika.ExpenseTracker.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    /**
     * Create a new group
     * POST /api/groups
     * Request Body: { "name": "Trip to Goa", "memberIds": [1, 2, 3] }
     */
    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(@RequestBody GroupRequest request) {
        GroupResponse group = groupService.createGroup(request);
        return ResponseEntity.ok(group);
    }

    /**
     * Get a specific group by ID
     * GET /api/groups/{id}
     */
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponse> getGroup(@PathVariable Long groupId) {
        GroupResponse group = groupService.getGroupById(groupId);
        return ResponseEntity.ok(group);
    }

    /**
     * Get all groups a user is part of
     * GET /api/groups/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GroupResponse>> getGroupsByUser(@PathVariable Long userId) {
        List<GroupResponse> groups = groupService.getGroupsByUser(userId);
        return ResponseEntity.ok(groups);
    }

    /**
     * Add members to an existing group
     * POST /api/groups/{id}/members
     * Request Body: { "memberIds": [4, 5] }
     */
    @PostMapping("/{groupId}/members")
    public ResponseEntity<GroupResponse> addMembers(
            @PathVariable Long groupId,
            @RequestBody GroupRequest request) {
        GroupResponse updatedGroup = groupService.addMembers(groupId, request.getMemberIds());
        return ResponseEntity.ok(updatedGroup);
    }
}
