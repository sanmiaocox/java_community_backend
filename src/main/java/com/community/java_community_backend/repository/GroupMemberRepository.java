package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    /** 查询群组的所有成员 */
    List<GroupMember> findByGroupIdOrderByJoinedAtAsc(Long groupId);

    /** 查询用户加入的所有群组的成员记录 */
    List<GroupMember> findByUserId(Long userId);

    /** 查询用户在某群的成员记录 */
    Optional<GroupMember> findByGroupIdAndUserId(Long groupId, Long userId);

    /** 检查用户是否在群中 */
    boolean existsByGroupIdAndUserId(Long groupId, Long userId);

    /** 统计群成员数 */
    long countByGroupId(Long groupId);

    /** 删除群成员记录 */
    void deleteByGroupIdAndUserId(Long groupId, Long userId);

    /**
     * 批量增加群内所有成员（除发送者外）的未读数
     */
    @Modifying
    @Query("UPDATE GroupMember gm SET gm.unreadCount = gm.unreadCount + 1 " +
           "WHERE gm.groupId = :groupId AND gm.userId != :senderId")
    void incrementUnreadExcludeSender(@Param("groupId") Long groupId, @Param("senderId") Long senderId);

    /** 清零某用户在某群的未读数 */
    @Modifying
    @Query("UPDATE GroupMember gm SET gm.unreadCount = 0 " +
           "WHERE gm.groupId = :groupId AND gm.userId = :userId")
    void clearUnread(@Param("groupId") Long groupId, @Param("userId") Long userId);
}

