package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.Favorite;
import com.community.java_community_backend.enums.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 收藏项Repository
 */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    
    /**
     * 查询收藏夹中的所有收藏项
     */
    List<Favorite> findByCollectionIdOrderByCreatedAtDesc(Long collectionId);
    
    /**
     * 查询收藏夹中指定类型的收藏项
     */
    List<Favorite> findByCollectionIdAndItemTypeOrderByCreatedAtDesc(Long collectionId, ItemType itemType);
    
    /**
     * 查询用户的所有收藏项
     */
    List<Favorite> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * 检查收藏项是否存在
     */
    boolean existsByCollectionIdAndItemTypeAndItemId(Long collectionId, ItemType itemType, Long itemId);
    
    /**
     * 查询指定收藏项
     */
    Optional<Favorite> findByCollectionIdAndItemTypeAndItemId(Long collectionId, ItemType itemType, Long itemId);
    
    /**
     * 统计收藏夹中的收藏项数量
     */
    long countByCollectionId(Long collectionId);
    
    /**
     * 统计收藏夹中指定类型的收藏项数量
     */
    long countByCollectionIdAndItemType(Long collectionId, ItemType itemType);
    
    /**
     * 删除收藏项
     */
    @Transactional
    @Modifying
    void deleteByCollectionIdAndItemTypeAndItemId(Long collectionId, ItemType itemType, Long itemId);
    
    /**
     * 检查用户是否收藏了某个项目（在任意收藏夹中）
     */
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Favorite f " +
           "WHERE f.user.id = :userId AND f.itemType = :itemType AND f.itemId = :itemId")
    boolean existsByUserIdAndItem(@Param("userId") Long userId, 
                                   @Param("itemType") ItemType itemType, 
                                   @Param("itemId") Long itemId);
    
    /**
     * 查询用户收藏某个项目的所有收藏夹
     */
    @Query("SELECT f FROM Favorite f WHERE f.user.id = :userId AND f.itemType = :itemType AND f.itemId = :itemId")
    List<Favorite> findByUserIdAndItem(@Param("userId") Long userId, 
                                        @Param("itemType") ItemType itemType, 
                                        @Param("itemId") Long itemId);
}
