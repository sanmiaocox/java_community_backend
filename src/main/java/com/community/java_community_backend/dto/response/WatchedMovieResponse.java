package com.community.java_community_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 看过记录响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WatchedMovieResponse {
    
    private Long id;
    private Long userId;
    private Long movieId;
    private LocalDateTime watchedAt;
    private Double rating;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 电影信息
    private MovieSimpleInfo movieInfo;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MovieSimpleInfo {
        private Long id;
        private String title;
        private String posterUrl;
        private Double rating;
        private String year;
    }
}

