package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    
    // 获取用户的收藏列表
    Page<Favorite> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    // 检查用户是否已收藏某电影
    boolean existsByUserIdAndMovieId(Long userId, Long movieId);
    
    // 查找用户对某电影的收藏记录
    Optional<Favorite> findByUserIdAndMovieId(Long userId, Long movieId);
    
    // 删除用户对某电影的收藏
    void deleteByUserIdAndMovieId(Long userId, Long movieId);
}

