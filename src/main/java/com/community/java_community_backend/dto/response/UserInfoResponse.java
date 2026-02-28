package com.community.java_community_backend.dto.response;

import com.community.java_community_backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户信息响应DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    
    private Long id;
    private String userCode;
    private String username;
    private String phone;
    private String avatar;
    private String bio;
    private LocalDateTime createdAt;
    
    /**
     * 从User实体转换为DTO
     */
    public static UserInfoResponse fromEntity(User user) {
        UserInfoResponse response = new UserInfoResponse();
        response.setId(user.getId());
        response.setUserCode(user.getUserCode());
        response.setUsername(user.getUsername());
        response.setPhone(user.getPhone());
        response.setAvatar(user.getAvatar());
        response.setBio(user.getBio());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}

