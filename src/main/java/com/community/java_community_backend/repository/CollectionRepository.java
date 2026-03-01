package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.Collection;
import com.community.java_community_backend.enums.CollectionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 收藏夹Repository
 */
@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    
    /**
     * 查询用户的所有收藏夹
     */
    List<Collection> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * 查询用户指定类型的收藏夹
     */
    List<Collection> findByUserIdAndTypeOrderByCreatedAtDesc(Long userId, CollectionType type);
    
    /**
     * 查询用户的系统收藏夹
     */
    List<Collection> findByUserIdAndIsSystemTrue(Long userId);
    
    /**
     * 查询用户指定类型的系统收藏夹
     */
    Optional<Collection> findByUserIdAndTypeAndIsSystemTrue(Long userId, CollectionType type);
    
    /**
     * 查询用户的公开收藏夹
     */
    List<Collection> findByUserIdAndIsPublicTrueOrderByCreatedAtDesc(Long userId);
    
    /**
     * 检查收藏夹是否属于指定用户
     */
    boolean existsByIdAndUserId(Long id, Long userId);
    
    /**
     * 统计用户的收藏夹数量
     */
    long countByUserId(Long userId);
}

