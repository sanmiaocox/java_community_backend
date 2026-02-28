package com.community.java_community_backend.controller;

import com.community.java_community_backend.dto.response.ApiResponse;
import com.community.java_community_backend.dto.response.FollowStatusResponse;
import com.community.java_community_backend.dto.response.UserInfoResponse;
import com.community.java_community_backend.service.FollowService;
import com.community.java_community_backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 关注控制器
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FollowController {
    
    private final FollowService followService;
    private final JwtUtil jwtUtil;
    
    /**
     * 关注用户
     */
    @PostMapping("/users/{userId}/follow")
    public ApiResponse<Void> followUser(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long userId) {
        String token = authHeader.replace("Bearer ", "");
        Long currentUserId = jwtUtil.getUserIdFromToken(token);
        
        followService.followUser(currentUserId, userId);
        return ApiResponse.success("关注成功", null);
    }
    
    /**
     * 取消关注
     */
    @DeleteMapping("/users/{userId}/follow")
    public ApiResponse<Void> unfollowUser(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long userId) {
        String token = authHeader.replace("Bearer ", "");
        Long currentUserId = jwtUtil.getUserIdFromToken(token);
        
        followService.unfollowUser(currentUserId, userId);
        return ApiResponse.success("取消关注成功", null);
    }
    
    /**
     * 获取关注列表（我关注的人）
     */
    @GetMapping("/users/{userId}/following")
    public ApiResponse<Page<UserInfoResponse>> getFollowingList(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<UserInfoResponse> followingList = followService.getFollowingList(userId, page, size);
        return ApiResponse.success("获取成功", followingList);
    }
    
    /**
     * 获取粉丝列表（关注我的人）
     */
    @GetMapping("/users/{userId}/followers")
    public ApiResponse<Page<UserInfoResponse>> getFollowerList(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<UserInfoResponse> followerList = followService.getFollowerList(userId, page, size);
        return ApiResponse.success("获取成功", followerList);
    }
    
    /**
     * 获取好友列表（互相关注）
     */
    @GetMapping("/users/{userId}/friends")
    public ApiResponse<List<UserInfoResponse>> getFriendList(@PathVariable Long userId) {
        List<UserInfoResponse> friendList = followService.getFriendList(userId);
        return ApiResponse.success("获取成功", friendList);
    }
    
    /**
     * 获取关注状态
     */
    @GetMapping("/users/{userId}/follow/status")
    public ApiResponse<FollowStatusResponse> getFollowStatus(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long userId) {
        String token = authHeader.replace("Bearer ", "");
        Long currentUserId = jwtUtil.getUserIdFromToken(token);
        
        FollowStatusResponse status = followService.getFollowStatus(currentUserId, userId);
        return ApiResponse.success("获取成功", status);
    }
    
    /**
     * 获取用户统计信息
     */
    @GetMapping("/users/{userId}/stats")
    public ApiResponse<Map<String, Long>> getUserStats(@PathVariable Long userId) {
        long followingCount = followService.countFollowing(userId);
        long followerCount = followService.countFollowers(userId);
        long friendCount = followService.countFriends(userId);
        
        Map<String, Long> stats = Map.of(
            "followingCount", followingCount,
            "followerCount", followerCount,
            "friendCount", friendCount
        );
        
        return ApiResponse.success("获取成功", stats);
    }
}

