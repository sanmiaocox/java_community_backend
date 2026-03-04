package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.EventParticipant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {
    
    // 检查用户是否已参加活动
    boolean existsByEventIdAndUserId(Long eventId, Long userId);
    
    // 查找用户参加的某个活动记录
    Optional<EventParticipant> findByEventIdAndUserId(Long eventId, Long userId);
    
    // 查询活动的所有参与者
    List<EventParticipant> findByEventIdOrderByJoinedAtAsc(Long eventId);
    
    // 查询用户参加的所有活动（分页）
    @Query("SELECT ep FROM EventParticipant ep WHERE ep.user.id = :userId ORDER BY ep.joinedAt DESC")
    Page<EventParticipant> findByUserIdOrderByJoinedAtDesc(@Param("userId") Long userId, Pageable pageable);
    
    // 统计活动参与人数
    long countByEventId(Long eventId);
}
