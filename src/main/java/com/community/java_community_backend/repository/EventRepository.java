package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    // 分页查询所有活动
    Page<Event> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    // 根据类型查询活动
    Page<Event> findByTypeOrderByCreatedAtDesc(String type, Pageable pageable);
    
    // 根据电影ID查询活动
    Page<Event> findByMovieIdOrderByCreatedAtDesc(Long movieId, Pageable pageable);
    
    // 搜索活动（标题或描述包含关键词）
    @Query("SELECT e FROM Event e WHERE e.title LIKE %:keyword% OR e.description LIKE %:keyword% ORDER BY e.createdAt DESC")
    Page<Event> searchEvents(@Param("keyword") String keyword, Pageable pageable);
}
