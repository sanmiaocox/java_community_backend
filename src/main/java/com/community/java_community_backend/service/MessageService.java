package com.community.java_community_backend.service;

import com.community.java_community_backend.dto.request.SendMessageRequest;
import com.community.java_community_backend.dto.response.MessageResponse;
import com.community.java_community_backend.entity.*;
import com.community.java_community_backend.exception.BusinessException;
import com.community.java_community_backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 消息服务（私聊与群聊）
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationService conversationService;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    // ===================== 发送消息 =====================

    /**
     * 发送消息（私聊或群聊统一入口）
     */
    @Transactional
    public MessageResponse sendMessage(Long senderId, SendMessageRequest request) {
        Message message;

        if (request.getChatType() == Message.ChatType.PRIVATE) {
            message = sendPrivateMessage(senderId, request);
        } else if (request.getChatType() == Message.ChatType.GROUP) {
            message = sendGroupMessage(senderId, request);
        } else {
            throw new BusinessException(400, "不支持的聊天类型");
        }

        return toResponse(message);
    }

    /**
     * 发送私信
     */
    private Message sendPrivateMessage(Long senderId, SendMessageRequest request) {
        if (request.getTargetUserId() == null) {
            throw new BusinessException(400, "私信必须指定目标用户ID");
        }

        // 获取或创建会话
        Conversation conv = conversationService.getOrCreateConversation(senderId, request.getTargetUserId());

        // 保存消息
        Message message = Message.builder()
                .chatType(Message.ChatType.PRIVATE)
                .conversationId(conv.getId())
                .senderId(senderId)
                .content(request.getContent())
                .type(request.getType() != null ? request.getType() : Message.MessageType.TEXT)
                .isRecalled(false)
                .build();
        message = messageRepository.save(message);

        // 更新会话摘要和对方未读数
        conversationService.updateAfterMessage(conv.getId(), senderId, request.getContent());

        return message;
    }

    /**
     * 发送群消息
     */
    private Message sendGroupMessage(Long senderId, SendMessageRequest request) {
        if (request.getGroupId() == null) {
            throw new BusinessException(400, "群聊必须指定群组ID");
        }

        // 验证发送者是群成员
        if (!groupMemberRepository.existsByGroupIdAndUserId(request.getGroupId(), senderId)) {
            throw new BusinessException(403, "你不是该群成员");
        }

        // 保存消息
        Message message = Message.builder()
                .chatType(Message.ChatType.GROUP)
                .groupId(request.getGroupId())
                .senderId(senderId)
                .content(request.getContent())
                .type(request.getType() != null ? request.getType() : Message.MessageType.TEXT)
                .isRecalled(false)
                .build();
        message = messageRepository.save(message);

        // 除发送者外所有群成员未读数 +1
        groupMemberRepository.incrementUnreadExcludeSender(request.getGroupId(), senderId);

        return message;
    }

    // ===================== 查询消息 =====================

    /**
     * 查询私信会话的消息列表（分页，最新在前；同时清零当前用户未读数）
     */
    @Transactional
    public Page<MessageResponse> getPrivateMessages(Long conversationId, Long currentUserId, Pageable pageable) {
        // 清零未读数（需要写事务，不能用 readOnly = true）
        conversationService.clearUnread(conversationId, currentUserId);
        return messageRepository
                .findByConversationIdOrderByCreatedAtDesc(conversationId, pageable)
                .map(this::toResponse);
    }

    /**
     * 查询群聊消息列表（分页，最新在前）
     */
    @Transactional
    public Page<MessageResponse> getGroupMessages(Long groupId, Long currentUserId, Pageable pageable) {
        // 校验成员身份
        if (!groupMemberRepository.existsByGroupIdAndUserId(groupId, currentUserId)) {
            throw new BusinessException(403, "你不是该群成员");
        }
        // 清零未读
        groupMemberRepository.clearUnread(groupId, currentUserId);
        return messageRepository
                .findByGroupIdOrderByCreatedAtDesc(groupId, pageable)
                .map(this::toResponse);
    }

    // ===================== 撤回消息 =====================

    /**
     * 撤回消息（仅发送者本人可撤回）
     */
    @Transactional
    public void recallMessage(Long messageId, Long userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new BusinessException(404, "消息不存在"));

        if (!message.getSenderId().equals(userId)) {
            throw new BusinessException(403, "只能撤回自己发送的消息");
        }
        if (Boolean.TRUE.equals(message.getIsRecalled())) {
            throw new BusinessException(400, "消息已撤回");
        }

        message.setIsRecalled(true);
        messageRepository.save(message);
    }

    // ===================== 转换 =====================

    private MessageResponse toResponse(Message msg) {
        User sender = msg.getSender() != null ? msg.getSender()
                : userRepository.findById(msg.getSenderId()).orElse(null);

        MessageResponse.SenderDTO senderDTO = sender == null ? null
                : MessageResponse.SenderDTO.builder()
                        .id(sender.getId())
                        .username(sender.getUsername())
                        .avatar(sender.getAvatar())
                        .build();

        // 已撤回的消息内容替换为提示文本
        String content = Boolean.TRUE.equals(msg.getIsRecalled())
                ? "消息已撤回" : msg.getContent();

        return MessageResponse.builder()
                .id(msg.getId())
                .chatType(msg.getChatType())
                .sender(senderDTO)
                .content(content)
                .type(msg.getType())
                .isRecalled(msg.getIsRecalled())
                .createdAt(msg.getCreatedAt())
                .build();
    }
}


