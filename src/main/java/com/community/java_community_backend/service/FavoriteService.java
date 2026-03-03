package com.community.java_community_backend.service;

import com.community.java_community_backend.dto.request.AddFavoriteRequest;
import com.community.java_community_backend.dto.response.CollectionResponse;
import com.community.java_community_backend.dto.response.FavoriteItemResponse;
import com.community.java_community_backend.entity.*;
import com.community.java_community_backend.enums.CollectionType;
import com.community.java_community_backend.enums.ItemType;
import com.community.java_community_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 收藏项服务
 */
@Service
@RequiredArgsConstructor
public class FavoriteService {
    
    private final FavoriteRepository favoriteRepository;
    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final EventRepository eventRepository;
    private final MovieService movieService;
    
    /**
     * 添加收藏项
     */
    @Transactional
    public FavoriteItemResponse addFavorite(Long userId, AddFavoriteRequest request) {
        // 验证用户
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 验证收藏夹
        Collection collection = collectionRepository.findById(request.getCollectionId())
                .orElseThrow(() -> new RuntimeException("收藏夹不存在"));
        
        // 检查权限
        if (!collection.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权操作该收藏夹");
        }
        
        // 检查收藏夹类型限制：收藏夹类型必须与收藏项类型一致
        if (collection.getType() == CollectionType.MOVIE && request.getItemType() != ItemType.MOVIE) {
            throw new RuntimeException("电影收藏夹只能收藏电影");
        }
        if (collection.getType() == CollectionType.EVENT && request.getItemType() != ItemType.EVENT) {
            throw new RuntimeException("活动收藏夹只能收藏活动");
        }
        
        // 【集成TMDB】如果是电影类型，保存到本地数据库
        if (request.getItemType() == ItemType.MOVIE) {
            if (request.getTmdbId() == null) {
                throw new RuntimeException("电影的TMDB ID不能为空");
            }
            Movie movie = movieService.saveOrUpdateMovie(request.getTmdbId());
            request.setItemId(movie.getId()); // 使用本地ID
        } else {
            // 活动类型，验证活动是否存在
            if (request.getItemId() == null) {
                throw new RuntimeException("活动ID不能为空");
            }
            validateItem(request.getItemType(), request.getItemId());
        }
        
        // 检查是否已收藏
        if (favoriteRepository.existsByCollectionIdAndItemTypeAndItemId(
                request.getCollectionId(), request.getItemType(), request.getItemId())) {
            throw new RuntimeException("该项目已在收藏夹中");
        }
        
        // 创建收藏项
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setCollection(collection);
        favorite.setItemType(request.getItemType());
        favorite.setItemId(request.getItemId());
        favorite.setNote(request.getNote());
        
        Favorite saved = favoriteRepository.save(favorite);
        
        // 更新收藏夹计数
        collection.setItemCount(collection.getItemCount() + 1);
        collectionRepository.save(collection);
        
        return convertToResponse(saved);
    }
    
    /**
     * 获取收藏夹中的所有收藏项
     */
    public List<FavoriteItemResponse> getCollectionItems(Long collectionId, Long userId) {
        // 验证收藏夹
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("收藏夹不存在"));
        
        // 检查权限
        if (!collection.getUser().getId().equals(userId) && !collection.getIsPublic()) {
            throw new RuntimeException("无权访问该收藏夹");
        }
        
        List<Favorite> favorites = favoriteRepository.findByCollectionIdOrderByCreatedAtDesc(collectionId);
        return favorites.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取收藏夹中指定类型的收藏项
     */
    public List<FavoriteItemResponse> getCollectionItemsByType(Long collectionId, Long userId, ItemType itemType) {
        // 验证收藏夹
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("收藏夹不存在"));
        
        // 检查权限
        if (!collection.getUser().getId().equals(userId) && !collection.getIsPublic()) {
            throw new RuntimeException("无权访问该收藏夹");
        }
        
        List<Favorite> favorites = favoriteRepository.findByCollectionIdAndItemTypeOrderByCreatedAtDesc(collectionId, itemType);
        return favorites.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 移除收藏项
     */
    @Transactional
    public void removeFavorite(Long collectionId, ItemType itemType, Long itemId, Long userId) {
        // 验证收藏夹
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("收藏夹不存在"));
        
        // 检查权限
        if (!collection.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权操作该收藏夹");
        }
        
        // 检查收藏项是否存在
        if (!favoriteRepository.existsByCollectionIdAndItemTypeAndItemId(collectionId, itemType, itemId)) {
            throw new RuntimeException("收藏项不存在");
        }
        
        // 删除收藏项
        favoriteRepository.deleteByCollectionIdAndItemTypeAndItemId(collectionId, itemType, itemId);
        
        // 更新收藏夹计数
        collection.setItemCount(Math.max(0, collection.getItemCount() - 1));
        collectionRepository.save(collection);
    }
    
    /**
     * 检查用户是否收藏了某个项目
     */
    public boolean isFavorited(Long userId, ItemType itemType, Long itemId) {
        return favoriteRepository.existsByUserIdAndItem(userId, itemType, itemId);
    }
    
    /**
     * 获取用户收藏某个项目的所有收藏夹
     */
    public List<CollectionResponse> getFavoriteCollections(Long userId, ItemType itemType, Long itemId) {
        List<Favorite> favorites = favoriteRepository.findByUserIdAndItem(userId, itemType, itemId);
        return favorites.stream()
                .map(f -> {
                    Collection collection = f.getCollection();
                    return CollectionResponse.builder()
                            .id(collection.getId())
                            .userId(collection.getUser().getId())
                            .name(collection.getName())
                            .type(collection.getType())
                            .isSystem(collection.getIsSystem())
                            .isPublic(collection.getIsPublic())
                            .itemCount(collection.getItemCount())
                            .build();
                })
                .collect(Collectors.toList());
    }
    
    /**
     * 验证收藏项是否存在
     */
    private void validateItem(ItemType itemType, Long itemId) {
        switch (itemType) {
            case MOVIE:
                if (!movieRepository.existsById(itemId)) {
                    throw new RuntimeException("电影不存在");
                }
                break;
            case EVENT:
                if (!eventRepository.existsById(itemId)) {
                    throw new RuntimeException("活动不存在");
                }
                break;
            default:
                throw new RuntimeException("不支持的收藏项类型");
        }
    }
    
    /**
     * 转换为响应DTO
     */
    private FavoriteItemResponse convertToResponse(Favorite favorite) {
        FavoriteItemResponse response = FavoriteItemResponse.builder()
                .id(favorite.getId())
                .collectionId(favorite.getCollection().getId())
                .itemType(favorite.getItemType())
                .itemId(favorite.getItemId())
                .note(favorite.getNote())
                .createdAt(favorite.getCreatedAt())
                .build();
        
        // 加载关联对象的详细信息
        Object itemDetail = loadItemDetail(favorite.getItemType(), favorite.getItemId());
        response.setItemDetail(itemDetail);
        
        return response;
    }
    
    /**
     * 加载收藏项的详细信息
     */
    private Object loadItemDetail(ItemType itemType, Long itemId) {
        switch (itemType) {
            case MOVIE:
                return movieRepository.findById(itemId)
                        .map(movie -> {
                            Map<String, Object> detail = new HashMap<>();
                            detail.put("id", movie.getId());
                            detail.put("title", movie.getTitle());
                            detail.put("posterUrl", movie.getPosterUrl());
                            detail.put("rating", movie.getRating());
                            detail.put("year", movie.getYear());
                            return detail;
                        })
                        .orElse(null);
            case EVENT:
                return eventRepository.findById(itemId)
                        .map(event -> {
                            Map<String, Object> detail = new HashMap<>();
                            detail.put("id", event.getId());
                            detail.put("title", event.getTitle());
                            detail.put("imageUrl", event.getImageUrl());
                            detail.put("eventDate", event.getEventDate());
                            detail.put("location", event.getLocation());
                            return detail;
                        })
                        .orElse(null);
            default:
                return null;
        }
    }
}

