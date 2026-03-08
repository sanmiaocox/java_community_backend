package com.community.java_community_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 电影响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {
    
    private Long id;
    private Integer tmdbId;
    private String title;
    private String originalTitle;
    private String posterUrl;
    private Double rating;
    private String ratingSource;
    private String releaseDate;
    private String year;
    private String genres;
    private String genre;
    private String region;
    private String languages;
    private String directors;
    private String actors;
    private String synopsis;
    private String tmdbUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}



