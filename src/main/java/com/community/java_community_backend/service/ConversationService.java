package com.community.java_community_backend.service;

import com.community.java_community_backend.dto.response.ConversationResponse;
import com.community.java_community_backend.entity.Conversation;
import com.community.java_community_backend.entity.User;
import com.community.java_community_backend.exception.BusinessException;
import com.community.java_community_backend.repository.ConversationRepository;
import com.community.java_community_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 私信会话服务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    /**
     * 获取或创建两个用户之间的会话
     * 强制 user1Id = min(A,B)，保证唯一性
     */
    @Transactional
    public Conversation getOrCreateConversation(Long userAId, Long userBId) {
        if (userAId.equals(userBId)) {
            throw new BusinessException(400, "不能与自己发起会话");
        }
        if (!userRepository.existsById(userBId)) {
            throw new BusinessException(404, "目标用户不存在");
        }

        Long user1Id = Math.min(userAId, userBId);
        Long user2Id = Math.max(userAId, userBId);

        return conversationRepository.findByUser1IdAndUser2Id(user1Id, user2Id)
                .orElseGet(() -> {
                    Conversation conv = Conversation.builder()
                            .user1Id(user1Id)
                            .user2Id(user2Id)
                            .user1Unread(0)
                            .user2Unread(0)
                            .build();
                    return conversationRepository.save(conv);
                });
    }

    /**
     * 查询当前用户的会话列表（按最后消息时间倒序）
     */
    @Transactional(readOnly = true)
    public Page<ConversationResponse> getConversations(Long userId, Pageable pageable) {
        return conversationRepository.findByUserId(userId, pageable)
                .map(conv -> toResponse(conv, userId));
    }

    /**
     * 将当前用户在指定会话中的未读数清零
     */
    @Transactional
    public void clearUnread(Long conversationId, Long userId) {
        Conversation conv = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new BusinessException(404, "会话不存在"));

        if (conv.getUser1Id().equals(userId)) {
            conv.setUser1Unread(0);
        } else if (conv.getUser2Id().equals(userId)) {
            conv.setUser2Unread(0);
        } else {
            throw new BusinessException(403, "无权限操作此会话");
        }
        conversationRepository.save(conv);
    }

    /**
     * 发送消息后更新会话摘要和对方未读数（由 MessageService 调用）
     */
    @Transactional
    public void updateAfterMessage(Long conversationId, Long senderId, String content) {
        Conversation conv = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new BusinessException(404, "会话不存在"));

        // 更新摘要（截取前100字）
        String preview = content.length() > 100 ? content.substring(0, 100) : content;
        conv.setLastMessage(preview);
        conv.setLastMessageAt(java.time.LocalDateTime.now());

        // 对方未读 +1
        if (conv.getUser1Id().equals(senderId)) {
            conv.setUser2Unread(conv.getUser2Unread() + 1);
        } else {
            conv.setUser1Unread(conv.getUser1Unread() + 1);
        }
        conversationRepository.save(conv);
    }

    // ===================== 转换 =====================

    private ConversationResponse toResponse(Conversation conv, Long currentUserId) {
        // 确定对方用户ID
        Long otherUserId = conv.getUser1Id().equals(currentUserId)
                ? conv.getUser2Id() : conv.getUser1Id();

        User other = userRepository.findById(otherUserId).orElse(null);

        ConversationResponse.OtherUserDTO otherDTO = other == null ? null
                : ConversationResponse.OtherUserDTO.builder()
                        .id(other.getId())
                        .username(other.getUsername())
                        .avatar(other.getAvatar())
                        .userCode(other.getUserCode())
                        .build();

        // 当前用户的未读数
        int unread = conv.getUser1Id().equals(currentUserId)
                ? conv.getUser1Unread() : conv.getUser2Unread();

        return ConversationResponse.builder()
                .conversationId(conv.getId())
                .otherUser(otherDTO)
                .lastMessage(conv.getLastMessage())
                .lastMessageAt(conv.getLastMessageAt())
                .unreadCount(unread)
                .build();
    }
}
