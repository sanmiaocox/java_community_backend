package com.community.java_community_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 关注状态响应DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowStatusResponse {
    
    /**
     * 我是否关注了对方
     */
    private Boolean isFollowing;
    
    /**
     * 对方是否关注了我
     */
    private Boolean isFollower;
    
    /**
     * 是否为好友（互相关注）
     */
    private Boolean isFriend;
}

