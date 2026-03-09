package com.community.java_community_backend.controller;

import com.community.java_community_backend.dto.response.ApiResponse;
import com.community.java_community_backend.dto.response.LikeStatusResponse;
import com.community.java_community_backend.service.LikeService;
import com.community.java_community_backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 点赞控制器
 */
@RestController
@RequiredArgsConstructor
public class LikeController {
    
    private final LikeService likeService;
    private final JwtUtil jwtUtil;
    
    /**
     * 点赞动态
     * POST /api/feeds/{feedId}/like
     */
    @PostMapping("/api/feeds/{feedId}/like")
    public ApiResponse<LikeStatusResponse> likeFeed(
            @RequestHeader("Authorization") String token,
            @PathVariable Long feedId) {
        Long userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        LikeStatusResponse response = likeService.likeFeed(userId, feedId);
        return ApiResponse.success("点赞成功", response);
    }
    
    /**
     * 取消点赞动态
     * DELETE /api/feeds/{feedId}/like
     */
    @DeleteMapping("/api/feeds/{feedId}/like")
    public ApiResponse<LikeStatusResponse> unlikeFeed(
            @RequestHeader("Authorization") String token,
            @PathVariable Long feedId) {
        Long userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        LikeStatusResponse response = likeService.unlikeFeed(userId, feedId);
        return ApiResponse.success("取消点赞成功", response);
    }
    
    /**
     * 检查是否点赞动态
     * GET /api/feeds/{feedId}/like/status
     */
    @GetMapping("/api/feeds/{feedId}/like/status")
    public ApiResponse<LikeStatusResponse> checkFeedLikeStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable Long feedId) {
        Long userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        LikeStatusResponse response = likeService.checkFeedLikeStatus(userId, feedId);
        return ApiResponse.success(response);
    }
    
    /**
     * 点赞评论
     * POST /api/comments/{commentId}/like
     */
    @PostMapping("/api/comments/{commentId}/like")
    public ApiResponse<LikeStatusResponse> likeComment(
            @RequestHeader("Authorization") String token,
            @PathVariable Long commentId) {
        Long userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        LikeStatusResponse response = likeService.likeComment(userId, commentId);
        return ApiResponse.success("点赞成功", response);
    }
    
    /**
     * 取消点赞评论
     * DELETE /api/comments/{commentId}/like
     */
    @DeleteMapping("/api/comments/{commentId}/like")
    public ApiResponse<LikeStatusResponse> unlikeComment(
            @RequestHeader("Authorization") String token,
            @PathVariable Long commentId) {
        Long userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        LikeStatusResponse response = likeService.unlikeComment(userId, commentId);
        return ApiResponse.success("取消点赞成功", response);
    }
    
    /**
     * 检查是否点赞评论
     * GET /api/comments/{commentId}/like/status
     */
    @GetMapping("/api/comments/{commentId}/like/status")
    public ApiResponse<LikeStatusResponse> checkCommentLikeStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable Long commentId) {
        Long userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        LikeStatusResponse response = likeService.checkCommentLikeStatus(userId, commentId);
        return ApiResponse.success(response);
    }
}



