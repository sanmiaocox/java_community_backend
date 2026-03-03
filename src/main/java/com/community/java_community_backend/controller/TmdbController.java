package com.community.java_community_backend.controller;

import com.community.java_community_backend.dto.response.ApiResponse;
import com.community.java_community_backend.service.TmdbService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * TMDB电影接口控制器
 * 提供TMDB电影数据查询功能
 */
@RestController
@RequestMapping("/api/tmdb")
@RequiredArgsConstructor
public class TmdbController {
    
    private final TmdbService tmdbService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 搜索电影
     * GET /api/tmdb/search?keyword=星际穿越&page=1
     */
    @GetMapping("/search")
    public ApiResponse<Object> searchMovies(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page) {
        JsonNode result = tmdbService.searchMovies(keyword, page);
        if (result != null) {
            // 将JsonNode转换为Map，避免序列化问题
            Map<String, Object> data = objectMapper.convertValue(result, Map.class);
            return ApiResponse.success(data);
        }
        return ApiResponse.error("搜索电影失败");
    }
    
    /**
     * 获取热门电影
     * GET /api/tmdb/popular?page=1
     */
    @GetMapping("/popular")
    public ApiResponse<Object> getPopularMovies(
            @RequestParam(defaultValue = "1") int page) {
        JsonNode result = tmdbService.getPopularMovies(page);
        if (result != null) {
            // 将JsonNode转换为Map，避免序列化问题
            Map<String, Object> data = objectMapper.convertValue(result, Map.class);
            return ApiResponse.success(data);
        }
        return ApiResponse.error("获取热门电影失败");
    }
    
    /**
     * 获取正在上映的电影
     * GET /api/tmdb/now-playing?page=1
     */
    @GetMapping("/now-playing")
    public ApiResponse<Object> getNowPlayingMovies(
            @RequestParam(defaultValue = "1") int page) {
        JsonNode result = tmdbService.getNowPlayingMovies(page);
        if (result != null) {
            // 将JsonNode转换为Map，避免序列化问题
            Map<String, Object> data = objectMapper.convertValue(result, Map.class);
            return ApiResponse.success(data);
        }
        return ApiResponse.error("获取正在上映电影失败");
    }
    
    /**
     * 获取即将上映的电影
     * GET /api/tmdb/upcoming?page=1
     */
    @GetMapping("/upcoming")
    public ApiResponse<Object> getUpcomingMovies(
            @RequestParam(defaultValue = "1") int page) {
        JsonNode result = tmdbService.getUpcomingMovies(page);
        if (result != null) {
            // 将JsonNode转换为Map，避免序列化问题
            Map<String, Object> data = objectMapper.convertValue(result, Map.class);
            return ApiResponse.success(data);
        }
        return ApiResponse.error("获取即将上映电影失败");
    }
    
    /**
     * 获取高分电影
     * GET /api/tmdb/top-rated?page=1
     */
    @GetMapping("/top-rated")
    public ApiResponse<Object> getTopRatedMovies(
            @RequestParam(defaultValue = "1") int page) {
        JsonNode result = tmdbService.getTopRatedMovies(page);
        if (result != null) {
            // 将JsonNode转换为Map，避免序列化问题
            Map<String, Object> data = objectMapper.convertValue(result, Map.class);
            return ApiResponse.success(data);
        }
        return ApiResponse.error("获取高分电影失败");
    }
    
    /**
     * 获取电影详情
     * GET /api/tmdb/movie/{tmdbId}
     */
    @GetMapping("/movie/{tmdbId}")
    public ApiResponse<Object> getMovieDetails(@PathVariable int tmdbId) {
        JsonNode result = tmdbService.getMovieDetails(tmdbId);
        if (result != null) {
            // 将JsonNode转换为Map，避免序列化问题
            Map<String, Object> data = objectMapper.convertValue(result, Map.class);
            return ApiResponse.success(data);
        }
        return ApiResponse.error("获取电影详情失败");
    }
    
    /**
     * 获取电影演职人员
     * GET /api/tmdb/movie/{tmdbId}/credits
     */
    @GetMapping("/movie/{tmdbId}/credits")
    public ApiResponse<Object> getMovieCredits(@PathVariable int tmdbId) {
        JsonNode result = tmdbService.getMovieCredits(tmdbId);
        if (result != null) {
            // 将JsonNode转换为Map，避免序列化问题
            Map<String, Object> data = objectMapper.convertValue(result, Map.class);
            return ApiResponse.success(data);
        }
        return ApiResponse.error("获取演职人员失败");
    }
    
    /**
     * 获取电影图片
     * GET /api/tmdb/movie/{tmdbId}/images
     */
    @GetMapping("/movie/{tmdbId}/images")
    public ApiResponse<Object> getMovieImages(@PathVariable int tmdbId) {
        JsonNode result = tmdbService.getMovieImages(tmdbId);
        if (result != null) {
            // 将JsonNode转换为Map，避免序列化问题
            Map<String, Object> data = objectMapper.convertValue(result, Map.class);
            return ApiResponse.success(data);
        }
        return ApiResponse.error("获取电影图片失败");
    }
}

