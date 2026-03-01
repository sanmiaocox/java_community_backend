package com.community.java_community_backend.dto.response;

import com.community.java_community_backend.enums.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 收藏项响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteItemResponse {
    
    private Long id;
    private Long collectionId;
    private ItemType itemType;
    private Long itemId;
    private String note;
    private LocalDateTime createdAt;
    
    // 关联对象信息
    private Object itemDetail; // 电影或活动的详细信息
}

