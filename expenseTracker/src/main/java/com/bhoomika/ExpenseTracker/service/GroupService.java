// GroupService.java_v1
// Business logic for group operations
// Handles creation, fetching, and member management

package com.bhoomika.ExpenseTracker.service;

import com.bhoomika.ExpenseTracker.dto.GroupRequest;
import com.bhoomika.ExpenseTracker.dto.GroupResponse;
import com.bhoomika.ExpenseTracker.model.Group;
import com.bhoomika.ExpenseTracker.model.Members;
import com.bhoomika.ExpenseTracker.model.User;
import com.bhoomika.ExpenseTracker.repository.GroupRepository;
import com.bhoomika.ExpenseTracker.repository.MembersRepository;
import com.bhoomika.ExpenseTracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private MembersRepository membersRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new group and adds members
     */
    public GroupResponse createGroup(GroupRequest request) {
        User creator = userRepository.findById(request.getMemberIds().get(0))
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        Group group = new Group();
        group.setName(request.getName());
        group.setCreatedBy(creator);
        group.setCreatedDate(LocalDateTime.now());

        Group savedGroup = groupRepository.save(group);

        // Add members (including creator)
        List<Members> members = request.getMemberIds().stream()
                .map(userId -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
                    Members member = new Members();
                    member.setGroupId(savedGroup.getGroupId());
                    member.setUserId(user.getUserId());
                    return member;
                })
                .collect(Collectors.toList());

        membersRepository.saveAll(members);

        return mapToResponse(savedGroup, request.getMemberIds());
    }

    /**
     * Fetch group by ID
     */
    public GroupResponse getGroupById(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<Long> memberIds = membersRepository.findByGroupId(groupId)
                .stream()
                .map(Members::getUserId)
                .collect(Collectors.toList());

        return mapToResponse(group, memberIds);
    }

    /**
     * Get all groups a user belongs to
     */
    public List<GroupResponse> getGroupsByUser(Long userId) {
        List<Members> userMemberships = membersRepository.findByUserId(userId);
        return userMemberships.stream()
                .map(m -> getGroupById(m.getGroupId()))
                .collect(Collectors.toList());
    }

    /**
     * Add new members to an existing group
     */
    public GroupResponse addMembers(Long groupId, List<Long> memberIds) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<Members> newMembers = memberIds.stream()
                .map(userId -> {
                    if (membersRepository.existsByGroupIdAndUserId(groupId, userId)) {
                        return null; // Skip if already member
                    }
                    Members member = new Members();
                    member.setGroupId(groupId);
                    member.setUserId(userId);
                    return member;
                })
                .filter(m -> m != null)
                .collect(Collectors.toList());

        membersRepository.saveAll(newMembers);

        List<Long> allMemberIds = membersRepository.findByGroupId(groupId)
                .stream()
                .map(Members::getUserId)
                .collect(Collectors.toList());

        return mapToResponse(group, allMemberIds);
    }

    // Helper: Map Group entity to GroupResponse DTO
    private GroupResponse mapToResponse(Group group, List<Long> memberIds) {
        GroupResponse response = new GroupResponse();
        response.setGroupId(group.getGroupId());
        response.setName(group.getName());
        response.setCreatedBy(group.getCreatedBy().getUserId());
        response.setCreatedDate(group.getCreatedDate());
        response.setMemberIds(memberIds);
        return response;
    }
}
