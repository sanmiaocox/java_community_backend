package com.community.java_community_backend.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 更新活动请求DTO
 */
@Data
public class UpdateEventRequest {
    
    @Size(max = 200, message = "活动标题不能超过200个字符")
    private String title;
    
    private String imageUrl;
    
    private LocalDateTime eventDate;
    
    private LocalDateTime registrationDeadline;
    
    private LocalDateTime endTime;
    
    @Size(max = 200, message = "活动地点不能超过200个字符")
    private String location;
    
    private Integer maxParticipants;
    
    @Size(max = 50, message = "活动类型不能超过50个字符")
    private String type;
    
    @Size(max = 2000, message = "活动描述不能超过2000个字符")
    private String description;
    
    private Long movieId;
}

