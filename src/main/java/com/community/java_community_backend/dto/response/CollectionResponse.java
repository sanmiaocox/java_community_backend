package com.community.java_community_backend.dto.response;

import com.community.java_community_backend.enums.CollectionType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 收藏夹响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollectionResponse {
    
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private CollectionType type;
    private Boolean isSystem;
    private Boolean isPublic;
    private String coverImage;
    private Integer itemCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

