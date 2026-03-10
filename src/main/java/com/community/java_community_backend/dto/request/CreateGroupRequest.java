package com.community.java_community_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 创建群聊请求 DTO
 */
@Data
public class CreateGroupRequest {

    /** 群组名称 */
    @NotBlank(message = "群名称不能为空")
    private String name;

    /** 群组头像 URL（可选） */
    private String avatar;

    /** 初始邀请的成员用户ID列表（不含群主自己） */
    @NotEmpty(message = "至少邀请一名成员")
    private List<Long> memberIds;

    /** 最大成员数，默认100 */
    private Integer maxMembers = 100;
}

