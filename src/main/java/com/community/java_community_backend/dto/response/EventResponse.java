package com.community.java_community_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 活动响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    
    private Long id;
    private String title;
    private String imageUrl;
    private LocalDateTime eventDate;
    private String location;
    private Integer participants;
    private Integer maxParticipants;
    private String type;
    private String description;
    private Long movieId;
    private String movieTitle;
    private String moviePosterUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isParticipant; // 当前用户是否已参加
}

