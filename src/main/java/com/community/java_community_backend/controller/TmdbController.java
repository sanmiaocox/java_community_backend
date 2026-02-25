package com.community.java_community_backend.controller;

import com.community.java_community_backend.dto.response.ApiResponse;
import com.community.java_community_backend.service.TmdbService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;

/**
 * TMDB测试控制器
 * 用于测试TMDB API集成
 */
@RestController
@RequestMapping("/api/tmdb")
@RequiredArgsConstructor
public class TmdbController {
    
    private final TmdbService tmdbService;
    
    /**
     * 测试：获取热门电影
     */
    @GetMapping("/popular")
    public ApiResponse<JsonNode> getPopularMovies(
            @RequestParam(defaultValue = "1") int page) {
        JsonNode result = tmdbService.getPopularMovies(page);
        if (result != null) {
            return ApiResponse.success(result);
        }
        return ApiResponse.error("获取热门电影失败");
    }
    
    /**
     * 测试：获取电影详情
     */
    @GetMapping("/movie/{movieId}")
    public ApiResponse<JsonNode> getMovieDetails(@PathVariable int movieId) {
        JsonNode result = tmdbService.getMovieDetails(movieId);
        if (result != null) {
            return ApiResponse.success(result);
        }
        return ApiResponse.error("获取电影详情失败");
    }
    
    /**
     * 测试：搜索电影
     */
    @GetMapping("/search")
    public ApiResponse<JsonNode> searchMovies(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page) {
        JsonNode result = tmdbService.searchMovies(keyword, page);
        if (result != null) {
            return ApiResponse.success(result);
        }
        return ApiResponse.error("搜索电影失败");
    }
    
    /**
     * 测试：获取电影演职人员
     */
    @GetMapping("/movie/{movieId}/credits")
    public ApiResponse<JsonNode> getMovieCredits(@PathVariable int movieId) {
        JsonNode result = tmdbService.getMovieCredits(movieId);
        if (result != null) {
            return ApiResponse.success(result);
        }
        return ApiResponse.error("获取演职人员失败");
    }
    
    /**
     * 测试：获取正在上映的电影
     */
    @GetMapping("/now-playing")
    public ApiResponse<JsonNode> getNowPlayingMovies(
            @RequestParam(defaultValue = "1") int page) {
        JsonNode result = tmdbService.getNowPlayingMovies(page);
        if (result != null) {
            return ApiResponse.success(result);
        }
        return ApiResponse.error("获取正在上映电影失败");
    }
    
    /**
     * 测试：获取高分电影
     */
    @GetMapping("/top-rated")
    public ApiResponse<JsonNode> getTopRatedMovies(
            @RequestParam(defaultValue = "1") int page) {
        JsonNode result = tmdbService.getTopRatedMovies(page);
        if (result != null) {
            return ApiResponse.success(result);
        }
        return ApiResponse.error("获取高分电影失败");
    }
}

