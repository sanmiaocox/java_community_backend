package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // 获取指定动态的所有评论（分页）
    Page<Comment> findByFeedIdOrderByCreatedAtDesc(Long feedId, Pageable pageable);
    
    // 统计指定动态的评论数
    long countByFeedId(Long feedId);
}

