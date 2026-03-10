package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    /** 查询私信会话的消息列表（按时间倒序分页） */
    Page<Message> findByConversationIdOrderByCreatedAtDesc(Long conversationId, Pageable pageable);

    /** 查询群聊消息列表（按时间倒序分页） */
    Page<Message> findByGroupIdOrderByCreatedAtDesc(Long groupId, Pageable pageable);

    /** 统计私信会话的消息总数 */
    long countByConversationId(Long conversationId);

    /** 统计群聊消息总数 */
    long countByGroupId(Long groupId);
}

