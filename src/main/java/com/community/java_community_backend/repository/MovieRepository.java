package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    
    // 获取评分最高的电影（用于轮播）
    List<Movie> findTop5ByOrderByRatingDesc();
    
    // 按评分排序（用于周榜）
    Page<Movie> findAllByOrderByRatingDesc(Pageable pageable);
    
    // 搜索电影（标题、导演、演员）
    @Query("SELECT m FROM Movie m WHERE " +
           "m.title LIKE %:keyword% OR " +
           "m.directors LIKE %:keyword% OR " +
           "m.actors LIKE %:keyword%")
    Page<Movie> searchMovies(@Param("keyword") String keyword, Pageable pageable);
}

