package com.community.java_community_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 点赞状态响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeStatusResponse {
    
    private Boolean isLiked;
    
    private Integer likeCount;
}



