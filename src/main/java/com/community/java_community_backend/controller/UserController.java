package com.community.java_community_backend.controller;

import com.community.java_community_backend.dto.request.ChangePasswordRequest;
import com.community.java_community_backend.dto.request.ChangePhoneRequest;
import com.community.java_community_backend.dto.request.UpdateProfileRequest;
import com.community.java_community_backend.dto.response.ApiResponse;
import com.community.java_community_backend.dto.response.FeedResponse;
import com.community.java_community_backend.dto.response.UserInfoResponse;
import com.community.java_community_backend.service.FeedService;
import com.community.java_community_backend.service.UserService;
import com.community.java_community_backend.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 * 处理用户相关请求
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final FeedService feedService;
    private final JwtUtil jwtUtil;
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/profile")
    public ApiResponse<UserInfoResponse> getProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.getUserIdFromToken(token);
        UserInfoResponse user = userService.getUserById(userId);
        return ApiResponse.success("获取成功", user);
    }
    
    /**
     * 更新个人资料
     */
    @PutMapping("/profile")
    public ApiResponse<UserInfoResponse> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody UpdateProfileRequest request) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.getUserIdFromToken(token);
        UserInfoResponse user = userService.updateProfile(userId, request);
        return ApiResponse.success("更新成功", user);
    }
    
    /**
     * 获取指定用户信息
     */
    @GetMapping("/{userId}")
    public ApiResponse<UserInfoResponse> getUserById(@PathVariable Long userId) {
        UserInfoResponse user = userService.getUserById(userId);
        return ApiResponse.success("获取成功", user);
    }
    
    /**
     * 获取用户动态列表
     * GET /api/users/{userId}/feeds?page=0&size=20
     */
    @GetMapping("/{userId}/feeds")
    public ApiResponse<Page<FeedResponse>> getUserFeeds(
            @RequestHeader("Authorization") String token,
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Long currentUserId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        Pageable pageable = PageRequest.of(page, size);
        Page<FeedResponse> feeds = feedService.getUserFeeds(userId, currentUserId, pageable);
        return ApiResponse.success(feeds);
    }
    
    /**
     * 修改密码
     * PUT /api/users/password
     */
    @PutMapping("/password")
    public ApiResponse<Void> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody ChangePasswordRequest request) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.getUserIdFromToken(token);
        userService.changePassword(userId, request);
        return ApiResponse.success("密码修改成功", null);
    }
    
    /**
     * 修改手机号
     * PUT /api/users/phone
     */
    @PutMapping("/phone")
    public ApiResponse<Void> changePhone(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody ChangePhoneRequest request) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.getUserIdFromToken(token);
        userService.changePhone(userId, request);
        return ApiResponse.success("手机号修改成功", null);
    }
}

