package com.community.java_community_backend.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新收藏夹请求DTO
 */
@Data
public class UpdateCollectionRequest {
    
    @Size(max = 100, message = "收藏夹名称不能超过100个字符")
    private String name;
    
    @Size(max = 500, message = "描述不能超过500个字符")
    private String description;
    
    private Boolean isPublic;
    
    private String coverImage;
}

