package com.community.java_community_backend.controller;

import com.community.java_community_backend.dto.request.CreateCollectionRequest;
import com.community.java_community_backend.dto.request.UpdateCollectionRequest;
import com.community.java_community_backend.dto.response.ApiResponse;
import com.community.java_community_backend.dto.response.CollectionResponse;
import com.community.java_community_backend.enums.CollectionType;
import com.community.java_community_backend.service.CollectionService;
import com.community.java_community_backend.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收藏夹控制器
 */
@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
public class CollectionController {
    
    private final CollectionService collectionService;
    private final JwtUtil jwtUtil;
    
    /**
     * 创建收藏夹
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CollectionResponse>> createCollection(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateCollectionRequest request) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        CollectionResponse response = collectionService.createCollection(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * 获取用户的所有收藏夹
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CollectionResponse>>> getUserCollections(
            @RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        List<CollectionResponse> collections = collectionService.getUserCollections(userId);
        return ResponseEntity.ok(ApiResponse.success(collections));
    }
    
    /**
     * 获取用户指定类型的收藏夹
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<CollectionResponse>>> getUserCollectionsByType(
            @RequestHeader("Authorization") String token,
            @PathVariable CollectionType type) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        List<CollectionResponse> collections = collectionService.getUserCollectionsByType(userId, type);
        return ResponseEntity.ok(ApiResponse.success(collections));
    }
    
    /**
     * 获取收藏夹详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CollectionResponse>> getCollectionById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        CollectionResponse collection = collectionService.getCollectionById(id, userId);
        return ResponseEntity.ok(ApiResponse.success(collection));
    }
    
    /**
     * 更新收藏夹
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CollectionResponse>> updateCollection(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @Valid @RequestBody UpdateCollectionRequest request) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        CollectionResponse response = collectionService.updateCollection(id, userId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * 删除收藏夹
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCollection(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        collectionService.deleteCollection(id, userId);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }
}

