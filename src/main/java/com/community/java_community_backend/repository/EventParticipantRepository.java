package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.EventParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {
    
    // 检查用户是否已参加某活动
    boolean existsByEventIdAndUserId(Long eventId, Long userId);
    
    // 查找用户对某活动的参与记录
    Optional<EventParticipant> findByEventIdAndUserId(Long eventId, Long userId);
    
    // 删除用户对某活动的参与记录
    void deleteByEventIdAndUserId(Long eventId, Long userId);
    
    // 统计某活动的参与人数
    long countByEventId(Long eventId);
}

