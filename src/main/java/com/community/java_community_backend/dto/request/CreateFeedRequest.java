package com.community.java_community_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 发布动态请求
 */
@Data
public class CreateFeedRequest {
    
    @NotBlank(message = "动态内容不能为空")
    @Size(max = 2000, message = "动态内容最多2000字符")
    private String content;
    
    @Size(max = 4, message = "最多上传4张图片")
    private List<String> images;
    
    private Long movieId;  // 本地数据库电影ID，不是tmdbId
    
    private Long eventId;
}



