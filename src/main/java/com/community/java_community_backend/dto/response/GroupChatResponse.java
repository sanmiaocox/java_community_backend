package com.community.java_community_backend.dto.response;

import com.community.java_community_backend.entity.GroupMember;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 群聊响应 DTO
 */
@Data
@Builder
public class GroupChatResponse {

    private Long id;
    private String name;
    private String avatar;

    /** 群主信息 */
    private OwnerDTO owner;

    private Integer maxMembers;

    /** 当前成员数 */
    private Long memberCount;

    /** 当前用户在此群的未读数（查群列表时填写） */
    private Integer unreadCount;

    /** 当前用户角色（查群详情时填写） */
    private GroupMember.GroupRole myRole;

    /** 关联的活动ID（如果有） */
    private Long eventId;

    private LocalDateTime createdAt;

    /** 成员列表（查群详情时填写） */
    private List<MemberDTO> members;

    @Data
    @Builder
    public static class OwnerDTO {
        private Long id;
        private String username;
        private String avatar;
    }

    @Data
    @Builder
    public static class MemberDTO {
        private Long userId;
        private String username;
        private String avatar;
        private String userCode;
        private GroupMember.GroupRole role;
        private LocalDateTime joinedAt;
    }
}

