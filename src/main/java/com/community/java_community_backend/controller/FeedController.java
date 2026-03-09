package com.community.java_community_backend.controller;

import com.community.java_community_backend.dto.request.CreateFeedRequest;
import com.community.java_community_backend.dto.response.ApiResponse;
import com.community.java_community_backend.dto.response.FeedResponse;
import com.community.java_community_backend.service.FeedService;
import com.community.java_community_backend.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * 动态控制器
 */
@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class FeedController {
    
    private final FeedService feedService;
    private final JwtUtil jwtUtil;
    
    /**
     * 发布动态
     * POST /api/feeds
     */
    @PostMapping
    public ApiResponse<FeedResponse> createFeed(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateFeedRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        FeedResponse response = feedService.createFeed(userId, request);
        return ApiResponse.success("发布成功", response);
    }
    
    /**
     * 获取动态列表（关注人的动态）
     * GET /api/feeds?page=0&size=20
     */
    @GetMapping
    public ApiResponse<Page<FeedResponse>> getFollowingFeeds(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Long userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        Pageable pageable = PageRequest.of(page, size);
        Page<FeedResponse> feeds = feedService.getFollowingFeeds(userId, pageable);
        return ApiResponse.success(feeds);
    }
    
    /**
     * 获取动态详情
     * GET /api/feeds/{feedId}
     */
    @GetMapping("/{feedId}")
    public ApiResponse<FeedResponse> getFeedById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long feedId) {
        Long userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        FeedResponse response = feedService.getFeedById(feedId, userId);
        return ApiResponse.success(response);
    }
    
    /**
     * 删除动态
     * DELETE /api/feeds/{feedId}
     */
    @DeleteMapping("/{feedId}")
    public ApiResponse<Void> deleteFeed(
            @RequestHeader("Authorization") String token,
            @PathVariable Long feedId) {
        Long userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        feedService.deleteFeed(feedId, userId);
        return ApiResponse.success("删除成功", null);
    }
}



