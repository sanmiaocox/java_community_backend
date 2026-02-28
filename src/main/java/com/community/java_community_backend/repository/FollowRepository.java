package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    
    /**
     * 检查是否已关注
     */
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    /**
     * 查找关注关系
     */
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    /**
     * 删除关注关系
     */
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    /**
     * 统计关注数（我关注了多少人）
     */
    long countByFollowerId(Long followerId);
    
    /**
     * 统计粉丝数（多少人关注我）
     */
    long countByFollowingId(Long followingId);
    
    /**
     * 获取关注列表（我关注的人）
     */
    @Query("SELECT f.followingId FROM Follow f WHERE f.followerId = :userId")
    List<Long> findFollowingIdsByFollowerId(@Param("userId") Long userId);
    
    /**
     * 获取粉丝列表（关注我的人）
     */
    @Query("SELECT f.followerId FROM Follow f WHERE f.followingId = :userId")
    List<Long> findFollowerIdsByFollowingId(@Param("userId") Long userId);
    
    /**
     * 获取关注列表（分页）
     */
    Page<Follow> findByFollowerId(Long followerId, Pageable pageable);
    
    /**
     * 获取粉丝列表（分页）
     */
    Page<Follow> findByFollowingId(Long followingId, Pageable pageable);
    
    /**
     * 获取好友ID列表（互相关注）
     */
    @Query("SELECT f1.followingId FROM Follow f1 " +
           "WHERE f1.followerId = :userId " +
           "AND EXISTS (SELECT 1 FROM Follow f2 WHERE f2.followerId = f1.followingId AND f2.followingId = :userId)")
    List<Long> findFriendIds(@Param("userId") Long userId);
    
    /**
     * 统计好友数量
     */
    @Query("SELECT COUNT(f1) FROM Follow f1 " +
           "WHERE f1.followerId = :userId " +
           "AND EXISTS (SELECT 1 FROM Follow f2 WHERE f2.followerId = f1.followingId AND f2.followingId = :userId)")
    long countFriends(@Param("userId") Long userId);
}

