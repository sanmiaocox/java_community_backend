package com.community.java_community_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    
    private String token;
    private UserInfoResponse user;
}

