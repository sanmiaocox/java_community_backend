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
 * 群聊成员实体类
 */
@Entity
@Table(
    name = "group_members",
    uniqueConstraints = @UniqueConstraint(name = "uk_group_member", columnNames = {"group_id", "user_id"}),
    indexes = @Index(name = "idx_gm_user", columnList = "user_id")
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 群组ID */
    @Column(name = "group_id", nullable = false)
    private Long groupId;

    /** 成员用户ID */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** 成员角色 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private GroupRole role = GroupRole.MEMBER;

    /** 该成员在此群的未读消息数 */
    @Column(name = "unread_count", nullable = false)
    private Integer unreadCount = 0;

    @CreatedDate
    @Column(name = "joined_at", updatable = false)
    private LocalDateTime joinedAt;

    // ---- 懒加载关联 ----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", insertable = false, updatable = false)
    private GroupChat group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    /** 成员角色枚举 */
    public enum GroupRole {
        /** 群主 */
        OWNER,
        /** 管理员 */
        ADMIN,
        /** 普通成员 */
        MEMBER
    }
}

