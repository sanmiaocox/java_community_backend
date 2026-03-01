package com.community.java_community_backend.controller;

import com.community.java_community_backend.dto.request.AddFavoriteRequest;
import com.community.java_community_backend.dto.response.FavoriteItemResponse;
import com.community.java_community_backend.enums.ItemType;
import com.community.java_community_backend.service.FavoriteService;
import com.community.java_community_backend.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 收藏项控制器
 */
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    
    private final FavoriteService favoriteService;
    private final JwtUtil jwtUtil;
    
    /**
     * 添加收藏项
     */
    @PostMapping
    public ResponseEntity<FavoriteItemResponse> addFavorite(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody AddFavoriteRequest request) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        FavoriteItemResponse response = favoriteService.addFavorite(userId, request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取收藏夹中的所有收藏项
     */
    @GetMapping("/collection/{collectionId}")
    public ResponseEntity<List<FavoriteItemResponse>> getCollectionItems(
            @RequestHeader("Authorization") String token,
            @PathVariable Long collectionId) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        List<FavoriteItemResponse> items = favoriteService.getCollectionItems(collectionId, userId);
        return ResponseEntity.ok(items);
    }
    
    /**
     * 获取收藏夹中指定类型的收藏项
     */
    @GetMapping("/collection/{collectionId}/type/{itemType}")
    public ResponseEntity<List<FavoriteItemResponse>> getCollectionItemsByType(
            @RequestHeader("Authorization") String token,
            @PathVariable Long collectionId,
            @PathVariable ItemType itemType) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        List<FavoriteItemResponse> items = favoriteService.getCollectionItemsByType(collectionId, userId, itemType);
        return ResponseEntity.ok(items);
    }
    
    /**
     * 移除收藏项
     */
    @DeleteMapping("/collection/{collectionId}/item/{itemType}/{itemId}")
    public ResponseEntity<Void> removeFavorite(
            @RequestHeader("Authorization") String token,
            @PathVariable Long collectionId,
            @PathVariable ItemType itemType,
            @PathVariable Long itemId) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        favoriteService.removeFavorite(collectionId, itemType, itemId, userId);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 检查用户是否收藏了某个项目
     */
    @GetMapping("/check/{itemType}/{itemId}")
    public ResponseEntity<Map<String, Boolean>> checkFavorite(
            @RequestHeader("Authorization") String token,
            @PathVariable ItemType itemType,
            @PathVariable Long itemId) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        boolean isFavorited = favoriteService.isFavorited(userId, itemType, itemId);
        return ResponseEntity.ok(Map.of("isFavorited", isFavorited));
    }
}

