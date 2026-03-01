package com.community.java_community_backend.dto.request;

import com.community.java_community_backend.enums.ItemType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 添加收藏项请求DTO
 */
@Data
public class AddFavoriteRequest {
    
    @NotNull(message = "收藏夹ID不能为空")
    private Long collectionId;
    
    @NotNull(message = "收藏项类型不能为空")
    private ItemType itemType;
    
    @NotNull(message = "收藏项ID不能为空")
    private Long itemId;
    
    private String note;
}

