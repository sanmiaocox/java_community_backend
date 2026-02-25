package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    
    // 获取所有动态，按创建时间倒序
    Page<Feed> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    // 获取指定用户的动态
    Page<Feed> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}

