package com.community.java_community_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 动态响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedResponse {
    
    private Long id;
    
    private UserSimpleDTO user;
    
    private MovieSimpleDTO movie;
    
    private EventSimpleDTO event;
    
    private String content;
    
    private List<String> images;
    
    private Integer likeCount;
    
    private Integer commentCount;
    
    private Boolean isLiked;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    /**
     * 用户简要信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSimpleDTO {
        private Long id;
        private String userCode;
        private String username;
        private String avatar;
    }
    
    /**
     * 电影简要信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MovieSimpleDTO {
        private Long id;
        private Integer tmdbId;
        private String title;
        private String posterUrl;
        private Double rating;
    }
    
    /**
     * 活动简要信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventSimpleDTO {
        private Long id;
        private String title;
        private String imageUrl;
        private LocalDateTime eventDate;
    }
}



