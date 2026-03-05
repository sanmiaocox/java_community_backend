package com.community.java_community_backend.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 创建活动请求DTO
 */
@Data
public class CreateEventRequest {
    
    @NotBlank(message = "活动标题不能为空")
    @Size(max = 200, message = "活动标题不能超过200个字符")
    private String title;
    
    private String imageUrl;
    
    @NotNull(message = "活动时间不能为空")
    @Future(message = "活动时间必须是未来时间")
    private LocalDateTime eventDate;
    
    private LocalDateTime registrationDeadline;
    
    private LocalDateTime endTime;
    
    @NotBlank(message = "活动地点不能为空")
    @Size(max = 200, message = "活动地点不能超过200个字符")
    private String location;
    
    @NotNull(message = "最大参与人数不能为空")
    private Integer maxParticipants = 100;
    
    @NotBlank(message = "活动类型不能为空")
    @Size(max = 50, message = "活动类型不能超过50个字符")
    private String type;
    
    @Size(max = 2000, message = "活动描述不能超过2000个字符")
    private String description;
    
    @NotNull(message = "关联电影ID不能为空")
    private Long movieId;
}

