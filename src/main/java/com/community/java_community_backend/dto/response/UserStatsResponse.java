package com.community.java_community_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户统计信息响应DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatsResponse {
    
    /**
     * 关注数（我关注了多少人）
     */
    private Long followingCount;
    
    /**
     * 粉丝数（多少人关注我）
     */
    private Long followerCount;
    
    /**
     * 好友数（互相关注）
     */
    private Long friendCount;
    
    /**
     * 动态数
     */
    private Long feedCount;
}

