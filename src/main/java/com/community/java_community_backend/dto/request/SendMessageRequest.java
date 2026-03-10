package com.community.java_community_backend.dto.request;

import com.community.java_community_backend.entity.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 发送消息请求 DTO（私聊与群聊共用）
 */
@Data
public class SendMessageRequest {

    /**
     * 聊天类型：PRIVATE / GROUP
     */
    @NotNull(message = "聊天类型不能为空")
    private Message.ChatType chatType;

    /**
     * 对方用户ID（chatType=PRIVATE 时必填）
     */
    private Long targetUserId;

    /**
     * 群组ID（chatType=GROUP 时必填）
     */
    private Long groupId;

    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
    private String content;

    /**
     * 消息类型：TEXT / IMAGE，默认 TEXT
     */
    private Message.MessageType type = Message.MessageType.TEXT;
}

