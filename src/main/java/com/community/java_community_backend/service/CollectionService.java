package com.community.java_community_backend.service;

import com.community.java_community_backend.dto.request.CreateCollectionRequest;
import com.community.java_community_backend.dto.request.UpdateCollectionRequest;
import com.community.java_community_backend.dto.response.CollectionResponse;
import com.community.java_community_backend.entity.Collection;
import com.community.java_community_backend.entity.User;
import com.community.java_community_backend.enums.CollectionType;
import com.community.java_community_backend.repository.CollectionRepository;
import com.community.java_community_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 收藏夹服务
 */
@Service
@RequiredArgsConstructor
public class CollectionService {
    
    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;
    
    /**
     * 创建收藏夹
     */
    @Transactional
    public CollectionResponse createCollection(Long userId, CreateCollectionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        Collection collection = new Collection();
        collection.setUser(user);
        collection.setName(request.getName());
        collection.setDescription(request.getDescription());
        collection.setType(request.getType());
        collection.setIsSystem(false);
        collection.setIsPublic(request.getIsPublic());
        collection.setCoverImage(request.getCoverImage());
        collection.setItemCount(0);
        
        Collection saved = collectionRepository.save(collection);
        return convertToResponse(saved);
    }
    
    /**
     * 创建默认收藏夹
     */
    @Transactional
    public Collection createDefaultCollection(Long userId, String name, CollectionType type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 检查是否已存在该类型的系统收藏夹
        if (collectionRepository.findByUserIdAndTypeAndIsSystemTrue(userId, type).isPresent()) {
            throw new RuntimeException("该类型的默认收藏夹已存在");
        }
        
        Collection collection = new Collection();
        collection.setUser(user);
        collection.setName(name);
        collection.setType(type);
        collection.setIsSystem(true);
        collection.setIsPublic(true);
        collection.setItemCount(0);
        
        return collectionRepository.save(collection);
    }
    
    /**
     * 获取用户的所有收藏夹
     */
    public List<CollectionResponse> getUserCollections(Long userId) {
        List<Collection> collections = collectionRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return collections.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取用户指定类型的收藏夹
     */
    public List<CollectionResponse> getUserCollectionsByType(Long userId, CollectionType type) {
        List<Collection> collections = collectionRepository.findByUserIdAndTypeOrderByCreatedAtDesc(userId, type);
        return collections.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取收藏夹详情
     */
    public CollectionResponse getCollectionById(Long collectionId, Long userId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("收藏夹不存在"));
        
        // 检查权限：只能查看自己的收藏夹或公开的收藏夹
        if (!collection.getUser().getId().equals(userId) && !collection.getIsPublic()) {
            throw new RuntimeException("无权访问该收藏夹");
        }
        
        return convertToResponse(collection);
    }
    
    /**
     * 更新收藏夹
     */
    @Transactional
    public CollectionResponse updateCollection(Long collectionId, Long userId, UpdateCollectionRequest request) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("收藏夹不存在"));
        
        // 检查权限
        if (!collection.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权修改该收藏夹");
        }
        
        // 系统收藏夹不允许修改名称和类型
        if (collection.getIsSystem() && request.getName() != null) {
            throw new RuntimeException("系统收藏夹不允许修改名称");
        }
        
        if (request.getName() != null) {
            collection.setName(request.getName());
        }
        if (request.getDescription() != null) {
            collection.setDescription(request.getDescription());
        }
        if (request.getIsPublic() != null) {
            collection.setIsPublic(request.getIsPublic());
        }
        if (request.getCoverImage() != null) {
            collection.setCoverImage(request.getCoverImage());
        }
        
        Collection updated = collectionRepository.save(collection);
        return convertToResponse(updated);
    }
    
    /**
     * 删除收藏夹
     */
    @Transactional
    public void deleteCollection(Long collectionId, Long userId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("收藏夹不存在"));
        
        // 检查权限
        if (!collection.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权删除该收藏夹");
        }
        
        // 系统收藏夹不允许删除
        if (collection.getIsSystem()) {
            throw new RuntimeException("系统收藏夹不允许删除");
        }
        
        collectionRepository.delete(collection);
    }
    
    /**
     * 更新收藏夹的收藏项数量
     */
    @Transactional
    public void updateItemCount(Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("收藏夹不存在"));
        
        long count = collection.getFavorites() != null ? collection.getFavorites().size() : 0;
        collection.setItemCount((int) count);
        collectionRepository.save(collection);
    }
    
    /**
     * 获取用户的默认收藏夹
     */
    public Collection getDefaultCollection(Long userId, CollectionType type) {
        return collectionRepository.findByUserIdAndTypeAndIsSystemTrue(userId, type)
                .orElseThrow(() -> new RuntimeException("默认收藏夹不存在"));
    }
    
    /**
     * 转换为响应DTO
     */
    private CollectionResponse convertToResponse(Collection collection) {
        return CollectionResponse.builder()
                .id(collection.getId())
                .userId(collection.getUser().getId())
                .name(collection.getName())
                .description(collection.getDescription())
                .type(collection.getType())
                .isSystem(collection.getIsSystem())
                .isPublic(collection.getIsPublic())
                .coverImage(collection.getCoverImage())
                .itemCount(collection.getItemCount())
                .createdAt(collection.getCreatedAt())
                .updatedAt(collection.getUpdatedAt())
                .build();
    }
}

