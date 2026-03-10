package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat, Long> {

    /** 查询用户创建的所有群组 */
    List<GroupChat> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);
}

