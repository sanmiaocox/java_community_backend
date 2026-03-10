package com.community.java_community_backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 私信会话响应 DTO
 */
@Data
@Builder
public class ConversationResponse {

    private Long conversationId;

    /** 对方用户信息 */
    private OtherUserDTO otherUser;

    /** 最后一条消息摘要 */
    private String lastMessage;

    /** 最后消息时间 */
    private LocalDateTime lastMessageAt;

    /** 当前用户的未读消息数 */
    private Integer unreadCount;

    @Data
    @Builder
    public static class OtherUserDTO {
        private Long id;
        private String username;
        private String avatar;
        private String userCode;
    }
}

