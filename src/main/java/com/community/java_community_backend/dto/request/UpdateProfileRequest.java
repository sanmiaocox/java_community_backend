package com.community.java_community_backend.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新个人资料请求DTO
 */
@Data
public class UpdateProfileRequest {
    
    @Size(min = 2, max = 50, message = "用户名长度必须在2-50之间")
    private String username;
    
    @Size(max = 500, message = "头像URL长度不能超过500")
    private String avatar;
    
    @Size(max = 500, message = "个人简介长度不能超过500")
    private String bio;
}

