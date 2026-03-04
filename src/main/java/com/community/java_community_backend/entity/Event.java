package com.community.java_community_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动实体类
 */
@Entity
@Table(name = "events", indexes = {
    @Index(name = "idx_event_date", columnList = "event_date"),
    @Index(name = "idx_movie_id", columnList = "movie_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(length = 500)
    private String imageUrl;
    
    @Column(nullable = false)
    private LocalDateTime eventDate;
    
    @Column(nullable = false, length = 200)
    private String location;
    
    @Column(nullable = false)
    private Integer participants = 0;
    
    @Column(nullable = false)
    private Integer maxParticipants = 100;
    
    @Column(nullable = false, length = 50)
    private String type;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;
    
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    // 关系映射
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventParticipant> eventParticipants;
}
