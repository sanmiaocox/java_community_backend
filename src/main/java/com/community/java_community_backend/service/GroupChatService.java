package com.community.java_community_backend.service;

import com.community.java_community_backend.dto.request.CreateGroupRequest;
import com.community.java_community_backend.dto.response.GroupChatResponse;
import com.community.java_community_backend.entity.GroupChat;
import com.community.java_community_backend.entity.GroupMember;
import com.community.java_community_backend.entity.User;
import com.community.java_community_backend.exception.BusinessException;
import com.community.java_community_backend.repository.EventParticipantRepository;
import com.community.java_community_backend.repository.GroupChatRepository;
import com.community.java_community_backend.repository.GroupMemberRepository;
import com.community.java_community_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 群聊服务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GroupChatService {

    private final GroupChatRepository groupChatRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final EventParticipantRepository eventParticipantRepository;

    // ===================== 群组管理 =====================

    /**
     * 创建群聊，创建者自动成为群主并加入群组
     */
    @Transactional
    public GroupChatResponse createGroup(Long ownerId, CreateGroupRequest request) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));

        GroupChat group = GroupChat.builder()
                .name(request.getName())
                .avatar(request.getAvatar())
                .ownerId(ownerId)
                .eventId(request.getEventId())
                .maxMembers(request.getMaxMembers() != null ? request.getMaxMembers() : 100)
                .build();
        group = groupChatRepository.save(group);

        // 群主加入
        groupMemberRepository.save(GroupMember.builder()
                .groupId(group.getId())
                .userId(ownerId)
                .role(GroupMember.GroupRole.OWNER)
                .unreadCount(0)
                .build());

        // 邀请初始成员
        Long groupId = group.getId();
        List<GroupMember> members = new ArrayList<>();
        for (Long memberId : request.getMemberIds()) {
            if (memberId.equals(ownerId)) continue;
            if (!userRepository.existsById(memberId)) continue;
            members.add(GroupMember.builder()
                    .groupId(groupId)
                    .userId(memberId)
                    .role(GroupMember.GroupRole.MEMBER)
                    .unreadCount(0)
                    .build());
        }
        groupMemberRepository.saveAll(members);

        log.info("用户{}创建了群聊: {}", ownerId, group.getId());
        return toDetailResponse(group, ownerId);
    }

    /**
     * 主动加入群聊（无需邀请）
     */
    @Transactional
    public void joinGroup(Long groupId, Long userId) {
        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(404, "群聊不存在"));
        if (groupMemberRepository.existsByGroupIdAndUserId(groupId, userId)) {
            return; // 已是成员，幂等处理
        }
        long currentCount = groupMemberRepository.countByGroupId(groupId);
        if (currentCount >= group.getMaxMembers()) {
            throw new BusinessException(400, "群人数已达上限");
        }
        groupMemberRepository.save(GroupMember.builder()
                .groupId(groupId)
                .userId(userId)
                .role(GroupMember.GroupRole.MEMBER)
                .unreadCount(0)
                .build());
        log.info("用户{}加入了群聊{}", userId, groupId);
    }

    /**
     * 通过活动ID查询对应群聊
     */
    @Transactional(readOnly = true)
    public GroupChatResponse getGroupByEventId(Long eventId, Long currentUserId) {
        GroupChat group = groupChatRepository.findByEventId(eventId)
                .orElseThrow(() -> new BusinessException(404, "该活动暂无对应群聊"));
        return toDetailResponse(group, currentUserId);
    }

    /**
     * 获取群聊详情（含成员列表）
     */
    @Transactional(readOnly = true)
    public GroupChatResponse getGroupDetail(Long groupId, Long currentUserId) {
        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(404, "群聊不存在"));
        if (!groupMemberRepository.existsByGroupIdAndUserId(groupId, currentUserId)) {
            throw new BusinessException(403, "你不是该群成员");
        }
        return toDetailResponse(group, currentUserId);
    }

    /**
     * 查询当前用户加入的所有群聊
     */
    @Transactional(readOnly = true)
    public List<GroupChatResponse> getMyGroups(Long userId) {
        return groupMemberRepository.findByUserId(userId).stream()
                .map(membership -> {
                    GroupChat group = groupChatRepository.findById(membership.getGroupId()).orElse(null);
                    if (group == null) return null;
                    User owner = userRepository.findById(group.getOwnerId()).orElse(null);
                    return GroupChatResponse.builder()
                            .id(group.getId())
                            .name(group.getName())
                            .avatar(group.getAvatar())
                            .owner(owner == null ? null : GroupChatResponse.OwnerDTO.builder()
                                    .id(owner.getId())
                                    .username(owner.getUsername())
                                    .avatar(owner.getAvatar())
                                    .build())
                            .maxMembers(group.getMaxMembers())
                            .memberCount(groupMemberRepository.countByGroupId(group.getId()))
                            .unreadCount(membership.getUnreadCount())
                            .myRole(membership.getRole())
                            .eventId(group.getEventId())
                            .createdAt(group.getCreatedAt())
                            .build();
                })
                .filter(r -> r != null)
                .collect(Collectors.toList());
    }

    /**
     * 邀请成员加入群聊（群主或管理员）
     */
    @Transactional
    public void inviteMembers(Long groupId, Long operatorId, List<Long> userIds) {
        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(404, "群聊不存在"));

        GroupMember operator = groupMemberRepository.findByGroupIdAndUserId(groupId, operatorId)
                .orElseThrow(() -> new BusinessException(403, "你不是该群成员"));

        if (operator.getRole() == GroupMember.GroupRole.MEMBER) {
            throw new BusinessException(403, "只有群主或管理员可以邀请成员");
        }

        long currentCount = groupMemberRepository.countByGroupId(groupId);
        if (currentCount + userIds.size() > group.getMaxMembers()) {
            throw new BusinessException(400, "超过群人数上限");
        }

        for (Long userId : userIds) {
            if (groupMemberRepository.existsByGroupIdAndUserId(groupId, userId)) continue;
            if (!userRepository.existsById(userId)) continue;
            groupMemberRepository.save(GroupMember.builder()
                    .groupId(groupId)
                    .userId(userId)
                    .role(GroupMember.GroupRole.MEMBER)
                    .unreadCount(0)
                    .build());
        }
    }

    /**
     * 退出群聊（群主不能直接退出，需先转让或解散）
     */
    @Transactional
    public void leaveGroup(Long groupId, Long userId) {
        groupChatRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(404, "群聊不存在"));

        GroupMember member = groupMemberRepository.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(() -> new BusinessException(400, "你不是该群成员"));

        if (member.getRole() == GroupMember.GroupRole.OWNER) {
            throw new BusinessException(400, "群主请先转让群主后再退出，或直接解散群组");
        }
        groupMemberRepository.deleteByGroupIdAndUserId(groupId, userId);
    }

    /**
     * 解散群聊（仅群主）
     */
    @Transactional
    public void dismissGroup(Long groupId, Long userId) {
        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(404, "群聊不存在"));

        if (!group.getOwnerId().equals(userId)) {
            throw new BusinessException(403, "只有群主可以解散群聊");
        }
        groupChatRepository.delete(group);
    }

    /**
     * 获取群成员列表（分页）
     */
    @Transactional(readOnly = true)
    public Page<GroupChatResponse.MemberDTO> getGroupMembers(Long groupId, Long currentUserId, Pageable pageable) {
        // 验证当前用户是群成员
        if (!groupMemberRepository.existsByGroupIdAndUserId(groupId, currentUserId)) {
            throw new BusinessException(403, "你不是该群成员");
        }

        Page<GroupMember> memberPage = groupMemberRepository.findByGroupIdOrderByJoinedAtAsc(groupId, pageable);
        
        return memberPage.map(gm -> {
            User u = userRepository.findById(gm.getUserId()).orElse(null);
            if (u == null) return null;
            return GroupChatResponse.MemberDTO.builder()
                    .userId(u.getId())
                    .username(u.getUsername())
                    .avatar(u.getAvatar())
                    .userCode(u.getUserCode())
                    .role(gm.getRole())
                    .joinedAt(gm.getJoinedAt())
                    .build();
        });
    }

    /**
     * 踢出群成员（仅群主或管理员）
     * 如果群聊关联了活动，同时移除活动参与者
     */
    @Transactional
    public void removeMember(Long groupId, Long operatorId, Long targetUserId) {
        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(404, "群聊不存在"));

        // 验证操作者权限
        GroupMember operator = groupMemberRepository.findByGroupIdAndUserId(groupId, operatorId)
                .orElseThrow(() -> new BusinessException(403, "你不是该群成员"));

        if (operator.getRole() == GroupMember.GroupRole.MEMBER) {
            throw new BusinessException(403, "只有群主或管理员可以踢出成员");
        }

        // 不能踢出群主
        if (group.getOwnerId().equals(targetUserId)) {
            throw new BusinessException(400, "不能踢出群主");
        }

        // 验证目标用户是群成员
        GroupMember target = groupMemberRepository.findByGroupIdAndUserId(groupId, targetUserId)
                .orElseThrow(() -> new BusinessException(400, "该用户不是群成员"));

        // 删除群成员
        groupMemberRepository.deleteByGroupIdAndUserId(groupId, targetUserId);

        // 如果群聊关联了活动，同时移除活动参与者
        if (group.getEventId() != null) {
            eventParticipantRepository.findByEventIdAndUserId(group.getEventId(), targetUserId)
                    .ifPresent(eventParticipantRepository::delete);
        }

        log.info("用户{}被踢出群聊{}，操作者{}", targetUserId, groupId, operatorId);
    }

    // ===================== 转换 =====================

    private GroupChatResponse toDetailResponse(GroupChat group, Long currentUserId) {
        List<GroupMember> memberList = groupMemberRepository.findByGroupIdOrderByJoinedAtAsc(group.getId());

        List<GroupChatResponse.MemberDTO> memberDTOs = memberList.stream().map(gm -> {
            User u = userRepository.findById(gm.getUserId()).orElse(null);
            if (u == null) return null;
            return GroupChatResponse.MemberDTO.builder()
                    .userId(u.getId())
                    .username(u.getUsername())
                    .avatar(u.getAvatar())
                    .userCode(u.getUserCode())
                    .role(gm.getRole())
                    .joinedAt(gm.getJoinedAt())
                    .build();
        }).filter(d -> d != null).collect(Collectors.toList());

        User owner = userRepository.findById(group.getOwnerId()).orElse(null);

        GroupMember myMembership = memberList.stream()
                .filter(gm -> gm.getUserId().equals(currentUserId))
                .findFirst().orElse(null);

        return GroupChatResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .avatar(group.getAvatar())
                .owner(owner == null ? null : GroupChatResponse.OwnerDTO.builder()
                        .id(owner.getId())
                        .username(owner.getUsername())
                        .avatar(owner.getAvatar())
                        .build())
                .maxMembers(group.getMaxMembers())
                .memberCount((long) memberList.size())
                .unreadCount(myMembership != null ? myMembership.getUnreadCount() : 0)
                .myRole(myMembership != null ? myMembership.getRole() : null)
                .eventId(group.getEventId())
                .createdAt(group.getCreatedAt())
                .members(memberDTOs)
                .build();
    }
}
