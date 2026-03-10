package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    /** 通过两个用户ID查找会话（user1Id < user2Id 已在 Service 层保证） */
    Optional<Conversation> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);

    /**
     * 查询某用户的所有会话列表，按最后消息时间倒序
     * user1 或 user2 都算该用户的会话
     */
    @Query("SELECT c FROM Conversation c WHERE (c.user1Id = :userId OR c.user2Id = :userId) " +
           "ORDER BY c.lastMessageAt DESC NULLS LAST")
    Page<Conversation> findByUserId(@Param("userId") Long userId, Pageable pageable);
}
