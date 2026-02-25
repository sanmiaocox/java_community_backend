package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    // 检查用户是否已点赞某动态
    boolean existsByUserIdAndFeedId(Long userId, Long feedId);
    
    // 查找用户对某动态的点赞记录
    Optional<Like> findByUserIdAndFeedId(Long userId, Long feedId);
    
    // 删除用户对某动态的点赞
    void deleteByUserIdAndFeedId(Long userId, Long feedId);
    
    // 统计某动态的点赞数
    long countByFeedId(Long feedId);
}

