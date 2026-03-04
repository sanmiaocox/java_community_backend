package com.community.java_community_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 活动参与者响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventParticipantResponse {
    
    private Long id;
    private Long userId;
    private String username;
    private String userCode;
    private String avatar;
    private LocalDateTime joinedAt;
}

