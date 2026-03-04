package com.community.java_community_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 活动参与者实体类
 */
@Entity
@Table(name = "event_participants", 
    uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "user_id"}),
    indexes = {
        @Index(name = "idx_event_id", columnList = "event_id"),
        @Index(name = "idx_user_id", columnList = "user_id")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class EventParticipant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime joinedAt;
}
