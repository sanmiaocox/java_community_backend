package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    // 检查用户是否已点赞某目标
    boolean existsByUserIdAndTargetTypeAndTargetId(Long userId, Like.TargetType targetType, Long targetId);
    
    // 查找用户对某目标的点赞记录
    Optional<Like> findByUserIdAndTargetTypeAndTargetId(Long userId, Like.TargetType targetType, Long targetId);
    
    // 删除用户对某目标的点赞
    void deleteByUserIdAndTargetTypeAndTargetId(Long userId, Like.TargetType targetType, Long targetId);
    
    // 统计某目标的点赞数
    long countByTargetTypeAndTargetId(Like.TargetType targetType, Long targetId);
    
    // 批量查询用户对多个目标的点赞状态
    List<Like> findByUserIdAndTargetTypeAndTargetIdIn(Long userId, Like.TargetType targetType, List<Long> targetIds);
}

