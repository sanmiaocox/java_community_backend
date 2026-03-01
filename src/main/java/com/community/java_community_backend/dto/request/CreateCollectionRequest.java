package com.community.java_community_backend.dto.request;

import com.community.java_community_backend.enums.CollectionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建收藏夹请求DTO
 */
@Data
public class CreateCollectionRequest {
    
    @NotBlank(message = "收藏夹名称不能为空")
    @Size(max = 100, message = "收藏夹名称不能超过100个字符")
    private String name;
    
    @Size(max = 500, message = "描述不能超过500个字符")
    private String description;
    
    @NotNull(message = "收藏夹类型不能为空")
    private CollectionType type;
    
    private Boolean isPublic = true;
    
    private String coverImage;
}

