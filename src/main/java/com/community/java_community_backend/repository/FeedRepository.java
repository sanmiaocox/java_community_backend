package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    
    // 获取关注人的动态（首页动态流）
    @Query("SELECT f FROM Feed f WHERE f.user.id IN :followingIds ORDER BY f.createdAt DESC")
    Page<Feed> findByUserIdIn(@Param("followingIds") List<Long> followingIds, Pageable pageable);
    
    // 获取指定用户的动态
    Page<Feed> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    // 获取指定电影的动态
    Page<Feed> findByMovieIdOrderByCreatedAtDesc(Long movieId, Pageable pageable);
    
    // 统计用户动态数
    long countByUserId(Long userId);
}

