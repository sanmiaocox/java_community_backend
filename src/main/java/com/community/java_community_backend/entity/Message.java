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
 * 消息实体类（私聊与群聊共用）
 * 通过 chatType 区分：PRIVATE 使用 conversationId，GROUP 使用 groupId
 */
@Entity
@Table(
    name = "messages",
    indexes = {
        @Index(name = "idx_msg_conv",   columnList = "conversation_id, created_at"),
        @Index(name = "idx_msg_group",  columnList = "group_id, created_at"),
        @Index(name = "idx_msg_sender", columnList = "sender_id")
    }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 聊天类型：PRIVATE=私聊，GROUP=群聊 */
    @Enumerated(EnumType.STRING)
    @Column(name = "chat_type", nullable = false, length = 10)
    private ChatType chatType;

    /**
     * 私信会话ID（chatType=PRIVATE 时填写，GROUP 时为 null）
     */
    @Column(name = "conversation_id")
    private Long conversationId;

    /**
     * 群组ID（chatType=GROUP 时填写，PRIVATE 时为 null）
     */
    @Column(name = "group_id")
    private Long groupId;

    /** 发送者用户ID */
    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    /** 消息内容 */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /** 消息类型：TEXT / IMAGE */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MessageType type = MessageType.TEXT;

    /** 是否已撤回 */
    @Column(name = "is_recalled", nullable = false)
    private Boolean isRecalled = false;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // ---- 懒加载关联 ----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", insertable = false, updatable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", insertable = false, updatable = false)
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", insertable = false, updatable = false)
    private GroupChat group;

    /** 聊天类型枚举 */
    public enum ChatType {
        PRIVATE,
        GROUP
    }

    /** 消息类型枚举 */
    public enum MessageType {
        TEXT,
        IMAGE
    }
}

