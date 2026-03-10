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
 * 群聊实体类
 */
@Entity
@Table(
    name = "group_chats",
    indexes = @Index(name = "idx_group_owner", columnList = "owner_id")
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class GroupChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 群组名称 */
    @Column(nullable = false, length = 100)
    private String name;

    /** 群组头像 URL */
    @Column(length = 500)
    private String avatar;

    /** 群主用户ID */
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    /** 最大成员数，默认100 */
    @Column(name = "max_members", nullable = false)
    private Integer maxMembers = 100;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ---- 懒加载关联 ----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", insertable = false, updatable = false)
    private User owner;
}

