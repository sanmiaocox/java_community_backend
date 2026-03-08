package com.community.java_community_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 参加活动请求DTO
 */
@Data
public class JoinEventRequest {
    
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String participantPhone;
    
    @NotBlank(message = "昵称不能为空")
    @Size(max = 50, message = "昵称不能超过50个字符")
    private String participantNickname;
    
    @Size(max = 50, message = "微信号不能超过50个字符")
    private String participantWechat;
    
    @Size(max = 20, message = "QQ号不能超过20个字符")
    private String participantQq;
}


