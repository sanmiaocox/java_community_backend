package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    // 获取所有活动，按活动日期排序
    Page<Event> findAllByOrderByEventDateAsc(Pageable pageable);
    
    // 获取指定电影的相关活动
    Page<Event> findByMovieIdOrderByEventDateAsc(Long movieId, Pageable pageable);
}

