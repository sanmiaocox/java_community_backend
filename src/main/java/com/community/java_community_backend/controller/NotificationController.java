package com.community.java_community_backend.controller;

import com.community.java_community_backend.dto.response.ApiResponse;
import com.community.java_community_backend.dto.response.NotificationResponse;
import com.community.java_community_backend.service.NotificationService;
import com.community.java_community_backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

/**
 * 通知控制器
 * 基础路径: /api/notifications
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtUtil jwtUtil;

    /**
     * GET /api/notifications
     * 查询当前用户所有通知（分页）
     */
    @GetMapping
    public ApiResponse<Page<NotificationResponse>> getNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(extractToken(request));
        Page<NotificationResponse> data = notificationService.getNotifications(
                userId, PageRequest.of(page, size));
        return ApiResponse.success(data);
    }

    /**
     * GET /api/notifications/unread
     * 查询当前用户未读通知（分页）
     */
    @GetMapping("/unread")
    public ApiResponse<Page<NotificationResponse>> getUnreadNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(extractToken(request));
        Page<NotificationResponse> data = notificationService.getUnreadNotifications(
                userId, PageRequest.of(page, size));
        return ApiResponse.success(data);
    }

    /**
     * GET /api/notifications/unread/count
     * 获取未读通知数量
     */
    @GetMapping("/unread/count")
    public ApiResponse<Long> getUnreadCount(HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(extractToken(request));
        return ApiResponse.success(notificationService.getUnreadCount(userId));
    }

    /**
     * PUT /api/notifications/{id}/read
     * 将单条通知标记为已读
     */
    @PutMapping("/{id}/read")
    public ApiResponse<Void> markAsRead(@PathVariable Long id, HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(extractToken(request));
        notificationService.markAsRead(id, userId);
        return ApiResponse.success(null);
    }

    /**
     * PUT /api/notifications/read-all
     * 将所有通知标记为已读
     */
    @PutMapping("/read-all")
    public ApiResponse<Void> markAllAsRead(HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(extractToken(request));
        notificationService.markAllAsRead(userId);
        return ApiResponse.success(null);
    }

    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}

