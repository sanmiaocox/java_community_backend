package com.community.java_community_backend.dto.response;

import com.community.java_community_backend.entity.Notification;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知响应 DTO
 */
@Data
@Builder
public class NotificationResponse {

    private Long id;

    /** 通知类型 */
    private Notification.NotificationType type;

    /** 触发者信息（系统通知为 null） */
    private SenderDTO sender;

    /** 关联对象类型：FEED / COMMENT / USER / EVENT */
    private String targetType;

    /** 关联对象ID */
    private Long targetId;

    /** 通知摘要文本 */
    private String content;

    /** 是否已读 */
    private Boolean isRead;

    private LocalDateTime createdAt;

    @Data
    @Builder
    public static class SenderDTO {
        private Long id;
        private String username;
        private String avatar;
    }
}

