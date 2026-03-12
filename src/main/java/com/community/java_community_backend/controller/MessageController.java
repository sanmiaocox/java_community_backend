package com.community.java_community_backend.controller;

import com.community.java_community_backend.dto.request.CreateGroupRequest;
import com.community.java_community_backend.dto.request.SendMessageRequest;
import com.community.java_community_backend.dto.response.*;
import com.community.java_community_backend.service.ConversationService;
import com.community.java_community_backend.service.GroupChatService;
import com.community.java_community_backend.service.MessageService;
import com.community.java_community_backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息控制器（私聊 + 群聊）
 * 基础路径: /api/messages
 */
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final ConversationService conversationService;
    private final GroupChatService groupChatService;
    private final JwtUtil jwtUtil;

    // ==================== 私信会话 ====================

    /**
     * GET /api/messages/conversations
     * 查询当前用户的私信会话列表（按最后消息时间倒序）
     */
    @GetMapping("/conversations")
    public ApiResponse<Page<ConversationResponse>> getConversations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(extractToken(request));
        Page<ConversationResponse> data = conversationService.getConversations(
                userId, PageRequest.of(page, size));
        return ApiResponse.success(data);
    }

    /**
     * GET /api/messages/conversations/{conversationId}
     * 查询私信会话的消息列表（分页，同时清零未读数）
     */
    @GetMapping("/conversations/{conversationId}")
    public ApiResponse<Page<MessageResponse>> getPrivateMessages(
            @PathVariable Long conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(extractToken(request));
        Page<MessageResponse> data = messageService.getPrivateMessages(
                conversationId, userId, PageRequest.of(page, size));
        return ApiResponse.success(data);
    }

    // ==================== 群聊 ====================

    /**
     * POST /api/messages/groups
     * 创建群聊
     */
    @PostMapping("/groups")
    public ApiResponse<GroupChatResponse> createGroup(
            @Valid @RequestBody CreateGroupRequest req,
            HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(extractToken(request));
        return ApiResponse.success(groupChatService.createGroup(userId, req));
    }

    /**
     * GET /api/messages/groups
     * 查询当前用户加入的所有群聊
     */
    @GetMapping("/groups")
    public ApiResponse<List<GroupChatResponse>> getMyGroups(HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(extractToken(request));
        return ApiResponse.success(groupChatService.getMyGroups(userId));
    }

    /**
     * GET /api/messages/groups/event/{eventId}
     * 通过活动ID查询对应群聊
     */
    @GetMapping("/groups/event/{eventId}")
    public ApiResponse<GroupChatResponse> getGroupByEventId(
            @PathVariable Long eventId,
            HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(extractToken(request));
        return ApiResponse.success(groupChatService.getGroupByEventId(eventId, userId));
    }

    /**
     * GET /api/messages/groups/{groupId}
     * 查询群聊详情（含成员列表）
     */
    @GetMapping("/groups/{groupId}")
    public ApiResponse<GroupChatResponse> getGroupDetail(
            @PathVariable Long groupId,
            HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(extractToken(request));
        return ApiResponse.success(groupChatService.getGroupDetail(groupId, userId));
    }

    /**
     * GET /api/messages/groups/{groupId}/messages
     * 查询群聊消息列表（分页，同时清零未读数）
     */
    @GetMapping("/groups/{groupId}/messages")
    public ApiResponse<Page<MessageResponse>> getGroupMessages(
            @PathVariable Long groupId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(extractToken(request));
        Page<MessageResponse> data = messageService.getGroupMessages(
                groupId, userId, PageRequest.of(page, size));
        return ApiResponse.success(data);
    }

    /**
     * POST /api/messages/groups/{groupId}/invite
     * 邀请成员加入群聊（群主或管理员）
     */
    @PostMapping("/groups/{groupId}/invite")
    public ApiResponse<Void> inviteMembers(
            @PathVariable Long groupId,
            @RequestBody List<Long> userIds,
            HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(extractToken(request));
        groupChatService.inviteMembers(groupId, userId, userIds);
        return ApiResponse.success(null);
    }

    /**
     * POST /api/messages/groups/{groupId}/join
     * 主动加入群聊
     */
    @PostMapping("/groups/{groupId}/join")
    public ApiResponse<Void> joinGroup(
            @PathVariable Long groupId,
            HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(extractToken(request));
        groupChatService.joinGroup(groupId, userId);
        return ApiResponse.success(null);
    }

    /**
     * DELETE /api/messages/groups/{groupId}/leave
     * 退出群聊
     */
    @DeleteMapping("/groups/{groupId}/leave")
    public ApiResponse<Void> leaveGroup(
            @PathVariable Long groupId,
            HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(extractToken(request));
        groupChatService.leaveGroup(groupId, userId);
        return ApiResponse.success(null);
    }

    /**
     * DELETE /api/messages/groups/{groupId}
     * 解散群聊（仅群主）
     */
    @DeleteMapping("/groups/{groupId}")
    public ApiResponse<Void> dismissGroup(
            @PathVariable Long groupId,
            HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(extractToken(request));
        groupChatService.dismissGroup(groupId, userId);
        return ApiResponse.success(null);
    }

    /**
     * GET /api/messages/groups/{groupId}/members
     * 获取群成员列表（分页）
     */
    @GetMapping("/groups/{groupId}/members")
    public ApiResponse<Page<GroupChatResponse.MemberDTO>> getGroupMembers(
            @PathVariable Long groupId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(extractToken(request));
        Page<GroupChatResponse.MemberDTO> data = groupChatService.getGroupMembers(
                groupId, userId, PageRequest.of(page, size));
        return ApiResponse.success(data);
    }

    /**
     * DELETE /api/messages/groups/{groupId}/members/{userId}
     * 踢出群成员（仅群主或管理员）
     */
    @DeleteMapping("/groups/{groupId}/members/{userId}")
    public ApiResponse<Void> removeMember(
            @PathVariable Long groupId,
            @PathVariable Long userId,
            HttpServletRequest request) {
        Long operatorId = jwtUtil.getUserIdFromToken(extractToken(request));
        groupChatService.removeMember(groupId, operatorId, userId);
        return ApiResponse.success(null);
    }

    // ==================== 发送 & 撤回（私聊+群聊统一入口） ====================

    /**
     * POST /api/messages/send
     * 发送消息（私聊或群聊，由 chatType 区分）
     */
    @PostMapping("/send")
    public ApiResponse<MessageResponse> sendMessage(
            @Valid @RequestBody SendMessageRequest req,
            HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(extractToken(request));
        return ApiResponse.success(messageService.sendMessage(userId, req));
    }

    /**
     * DELETE /api/messages/{messageId}/recall
     * 撤回消息（仅发送者本人）
     */
    @DeleteMapping("/{messageId}/recall")
    public ApiResponse<Void> recallMessage(
            @PathVariable Long messageId,
            HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(extractToken(request));
        messageService.recallMessage(messageId, userId);
        return ApiResponse.success(null);
    }

    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}


