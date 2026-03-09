package com.community.java_community_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 发表评论请求
 */
@Data
public class CreateCommentRequest {
    
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 500, message = "评论内容最多500字符")
    private String content;
}



