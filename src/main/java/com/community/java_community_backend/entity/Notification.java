package com.community.java_community_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 系统通知实体类
 * 支持：点赞、评论、关注、活动报名/退出、系统公告
 */
@Entity
@Table(
    name = "notifications",
    indexes = {
        @Index(name = "idx_receiver_unread", columnList = "receiver_id, is_read"),
        @Index(name = "idx_receiver_time", columnList = "receiver_id, created_at")
    }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 接收者用户ID */
    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    /**
     * 触发者用户ID（系统通知时为 null）
     * 使用 ON DELETE SET NULL，用户注销后通知保留
     */
    @Column(name = "sender_id")
    private Long senderId;

    /** 通知类型 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type;

    /**
     * 关联对象类型：FEED / COMMENT / USER / EVENT
     * 可为 null（如系统公告）
     */
    @Column(name = "target_type", length = 20)
    private String targetType;

    /**
     * 关联对象ID
     * 可为 null（如系统公告）
     */
    @Column(name = "target_id")
    private Long targetId;

    /**
     * 通知摘要文本（如评论内容前50字）
     * 最多200字
     */
    @Column(length = 200)
    private String content;

    /** 是否已读 */
    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // ---- 懒加载关联 ----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", insertable = false, updatable = false)
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", insertable = false, updatable = false)
    private User sender;

    /**
     * 通知类型枚举
     */
    public enum NotificationType {
        /** 点赞动态 */
        LIKE_FEED,
        /** 点赞评论 */
        LIKE_COMMENT,
        /** 评论动态 */
        COMMENT_FEED,
        /** 关注 */
        FOLLOW,
        /** 活动报名 */
        EVENT_JOIN,
        /** 退出活动 */
        EVENT_QUIT,
        /** 系统公告 */
        SYSTEM
    }
}

