package com.community.java_community_backend.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 标记看过请求DTO
 */
@Data
public class MarkWatchedRequest {
    
    @NotNull(message = "电影ID不能为空")
    private Long movieId;
    
    @DecimalMin(value = "0.0", message = "评分不能小于0.0")
    @DecimalMax(value = "10.0", message = "评分不能大于10.0")
    private Double rating;
    
    private String note;
}

