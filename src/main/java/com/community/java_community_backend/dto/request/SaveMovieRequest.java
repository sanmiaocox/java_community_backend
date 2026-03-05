package com.community.java_community_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 保存电影请求
 */
@Data
public class SaveMovieRequest {
    
    @NotNull(message = "TMDB ID不能为空")
    private Integer tmdbId;
}


