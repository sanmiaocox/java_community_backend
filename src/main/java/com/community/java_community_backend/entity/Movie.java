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

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Movie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(length = 200)
    private String originalTitle;
    
    @Column(length = 500)
    private String aliases; // JSON数组字符串
    
    @Column(length = 500)
    private String posterUrl;
    
    @Column(length = 2000)
    private String posterUrls; // JSON数组字符串
    
    @Column
    private Double rating;
    
    @Column(length = 50)
    private String ratingSource;
    
    @Column(length = 100)
    private String releaseDate;
    
    @Column(length = 50)
    private String episodes;
    
    @Column(length = 200)
    private String genres; // JSON数组字符串
    
    @Column(length = 100)
    private String region;
    
    @Column(length = 200)
    private String languages; // JSON数组字符串
    
    @Column(length = 500)
    private String directors; // JSON数组字符串
    
    @Column(length = 1000)
    private String actors; // JSON数组字符串
    
    @Column(columnDefinition = "TEXT")
    private String synopsis;
    
    @Column(length = 500)
    private String doubanUrl;
    
    @Column(length = 500)
    private String tmdbUrl;
    
    @Column(length = 50)
    private String year;
    
    @Column(length = 50)
    private String genre; // 主要类型
    
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    // 关系映射
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Feed> feeds;
    
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Event> events;
}

