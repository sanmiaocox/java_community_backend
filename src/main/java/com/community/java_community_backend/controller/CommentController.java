package com.community.java_community_backend.controller;

import com.community.java_community_backend.dto.request.CreateCommentRequest;
import com.community.java_community_backend.dto.response.ApiResponse;
import com.community.java_community_backend.dto.response.CommentResponse;
import com.community.java_community_backend.service.CommentService;
import com.community.java_community_backend.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * 评论控制器
 */
@RestController
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;
    private final JwtUtil jwtUtil;
    
    /**
     * 发表评论
     * POST /api/feeds/{feedId}/comments
     */
    @PostMapping("/api/feeds/{feedId}/comments")
    public ApiResponse<CommentResponse> createComment(
            @RequestHeader("Authorization") String token,
            @PathVariable Long feedId,
            @Valid @RequestBody CreateCommentRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        CommentResponse response = commentService.createComment(userId, feedId, request);
        return ApiResponse.success("评论成功", response);
    }
    
    /**
     * 获取动态的评论列表
     * GET /api/feeds/{feedId}/comments?page=0&size=20
     */
    @GetMapping("/api/feeds/{feedId}/comments")
    public ApiResponse<Page<CommentResponse>> getFeedComments(
            @RequestHeader("Authorization") String token,
            @PathVariable Long feedId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Long userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentResponse> comments = commentService.getFeedComments(feedId, userId, pageable);
        return ApiResponse.success(comments);
    }
    
    /**
     * 删除评论
     * DELETE /api/comments/{commentId}
     */
    @DeleteMapping("/api/comments/{commentId}")
    public ApiResponse<Void> deleteComment(
            @RequestHeader("Authorization") String token,
            @PathVariable Long commentId) {
        Long userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        commentService.deleteComment(commentId, userId);
        return ApiResponse.success("删除成功", null);
    }
}



