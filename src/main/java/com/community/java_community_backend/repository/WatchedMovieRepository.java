package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.WatchedMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 看过记录Repository
 */
@Repository
public interface WatchedMovieRepository extends JpaRepository<WatchedMovie, Long> {
    
    /**
     * 查询用户看过的所有电影
     */
    List<WatchedMovie> findByUserIdOrderByWatchedAtDesc(Long userId);
    
    /**
     * 检查用户是否看过某部电影
     */
    boolean existsByUserIdAndMovieId(Long userId, Long movieId);
    
    /**
     * 查询用户看过某部电影的记录
     */
    Optional<WatchedMovie> findByUserIdAndMovieId(Long userId, Long movieId);
    
    /**
     * 删除看过记录
     */
    @Transactional
    @Modifying
    void deleteByUserIdAndMovieId(Long userId, Long movieId);
    
    /**
     * 统计用户看过的电影数量
     */
    long countByUserId(Long userId);
    
    /**
     * 统计电影被看过的次数
     */
    long countByMovieId(Long movieId);
}

