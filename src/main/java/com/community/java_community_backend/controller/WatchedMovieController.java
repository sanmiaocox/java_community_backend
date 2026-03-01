package com.community.java_community_backend.controller;

import com.community.java_community_backend.dto.request.MarkWatchedRequest;
import com.community.java_community_backend.dto.response.WatchedMovieResponse;
import com.community.java_community_backend.service.WatchedMovieService;
import com.community.java_community_backend.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 看过记录控制器
 */
@RestController
@RequestMapping("/api/watched")
@RequiredArgsConstructor
public class WatchedMovieController {
    
    private final WatchedMovieService watchedMovieService;
    private final JwtUtil jwtUtil;
    
    /**
     * 标记电影为看过
     */
    @PostMapping
    public ResponseEntity<WatchedMovieResponse> markAsWatched(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody MarkWatchedRequest request) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        WatchedMovieResponse response = watchedMovieService.markAsWatched(userId, request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 取消看过标记
     */
    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> unmarkAsWatched(
            @RequestHeader("Authorization") String token,
            @PathVariable Long movieId) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        watchedMovieService.unmarkAsWatched(userId, movieId);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 更新看过记录
     */
    @PutMapping("/{movieId}")
    public ResponseEntity<WatchedMovieResponse> updateWatchedMovie(
            @RequestHeader("Authorization") String token,
            @PathVariable Long movieId,
            @Valid @RequestBody MarkWatchedRequest request) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        WatchedMovieResponse response = watchedMovieService.updateWatchedMovie(userId, movieId, request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取用户看过的所有电影
     */
    @GetMapping
    public ResponseEntity<List<WatchedMovieResponse>> getUserWatchedMovies(
            @RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        List<WatchedMovieResponse> watchedMovies = watchedMovieService.getUserWatchedMovies(userId);
        return ResponseEntity.ok(watchedMovies);
    }
    
    /**
     * 检查用户是否看过某部电影
     */
    @GetMapping("/check/{movieId}")
    public ResponseEntity<Map<String, Boolean>> checkWatched(
            @RequestHeader("Authorization") String token,
            @PathVariable Long movieId) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        boolean isWatched = watchedMovieService.isWatched(userId, movieId);
        return ResponseEntity.ok(Map.of("isWatched", isWatched));
    }
    
    /**
     * 获取用户看过的电影数量
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getUserWatchedCount(
            @RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        long count = watchedMovieService.getUserWatchedCount(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }
}

