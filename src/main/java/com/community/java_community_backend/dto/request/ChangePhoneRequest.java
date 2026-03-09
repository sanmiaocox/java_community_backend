package com.community.java_community_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 修改手机号请求
 */
@Data
public class ChangePhoneRequest {
    
    @NotBlank(message = "新手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String newPhone;
    
    @NotBlank(message = "密码不能为空")
    private String password;
}

