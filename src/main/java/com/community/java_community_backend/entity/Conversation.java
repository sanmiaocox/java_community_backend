package com.community.java_community_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 私信会话实体类
 * 两个用户之间唯一一条会话记录，通过 user1_id < user2_id 保证唯一性
 */
@Entity
@Table(
    name = "conversations",
    uniqueConstraints = @UniqueConstraint(name = "uk_conv_users", columnNames = {"user1_id", "user2_id"}),
    indexes = {
        @Index(name = "idx_conv_user1", columnList = "user1_id, last_message_at"),
        @Index(name = "idx_conv_user2", columnList = "user2_id, last_message_at")
    }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 两个参与者中 ID 较小的用户（Service 层写入前强制 user1Id = min(A,B)）
     */
    @Column(name = "user1_id", nullable = false)
    private Long user1Id;

    /**
     * 两个参与者中 ID 较大的用户
     */
    @Column(name = "user2_id", nullable = false)
    private Long user2Id;

    /** 最后一条消息摘要（截取前100字） */
    @Column(name = "last_message", length = 200)
    private String lastMessage;

    /** 最后消息时间，用于会话列表排序 */
    @Column(name = "last_message_at")
    private LocalDateTime lastMessageAt;

    /** user1 的未读消息数 */
    @Column(name = "user1_unread", nullable = false)
    private Integer user1Unread = 0;

    /** user2 的未读消息数 */
    @Column(name = "user2_unread", nullable = false)
    private Integer user2Unread = 0;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ---- 懒加载关联 ----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id", insertable = false, updatable = false)
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id", insertable = false, updatable = false)
    private User user2;
}

