package com.community.java_community_backend.dto.response;

import com.community.java_community_backend.entity.Message;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息响应 DTO（私聊与群聊共用）
 */
@Data
@Builder
public class MessageResponse {

    private Long id;

    private Message.ChatType chatType;

    /** 发送者信息 */
    private SenderDTO sender;

    /** 消息内容（已撤回时返回固定文本） */
    private String content;

    private Message.MessageType type;

    /** 是否已撤回 */
    private Boolean isRecalled;

    private LocalDateTime createdAt;

    @Data
    @Builder
    public static class SenderDTO {
        private Long id;
        private String username;
        private String avatar;
    }
}

