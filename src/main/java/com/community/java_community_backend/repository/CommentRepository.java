package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // 获取指定动态的所有评论
    List<Comment> findByFeedIdOrderByCreatedAtDesc(Long feedId);
    
    // 统计指定动态的评论数
    long countByFeedId(Long feedId);
}

