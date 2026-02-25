package com.community.java_community_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * TMDB API服务
 * 用于从TMDB获取电影数据
 */
@Service
@Slf4j
public class TmdbService {
    
    @Value("${tmdb.api.key}")
    private String apiKey;
    
    @Value("${tmdb.api.read-token}")
    private String readToken;
    
    @Value("https://api.themoviedb.org/3")
    private String baseUrl;
    
    @Value("${tmdb.image.base-url}")
    private String imageBaseUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 创建请求头（使用Bearer Token认证）
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + readToken);
        headers.set("accept", "application/json");
        return headers;
    }
    
    /**
     * 获取热门电影列表
     * @param page 页码（从1开始）
     * @return JSON响应
     */
    public JsonNode getPopularMovies(int page) {
        try {
            String url = String.format("%s/movie/popular?language=zh-CN&page=%d", baseUrl, page);
            HttpEntity<String> entity = new HttpEntity<>(createHeaders());
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                String.class
            );
            
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            log.error("获取热门电影失败", e);
            return null;
        }
    }
    
    /**
     * 获取电影详情
     * @param movieId TMDB电影ID
     * @return JSON响应
     */
    public JsonNode getMovieDetails(int movieId) {
        try {
            String url = String.format("%s/movie/%d?language=zh-CN", baseUrl, movieId);
            HttpEntity<String> entity = new HttpEntity<>(createHeaders());
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                String.class
            );
            
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            log.error("获取电影详情失败: movieId={}", movieId, e);
            return null;
        }
    }
    
    /**
     * 搜索电影
     * @param keyword 搜索关键词
     * @param page 页码
     * @return JSON响应
     */
    public JsonNode searchMovies(String keyword, int page) {
        try {
            String url = String.format("%s/search/movie?query=%s&language=zh-CN&page=%d", 
                baseUrl, keyword, page);
            HttpEntity<String> entity = new HttpEntity<>(createHeaders());
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                String.class
            );
            
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            log.error("搜索电影失败: keyword={}", keyword, e);
            return null;
        }
    }
    
    /**
     * 获取电影演职人员信息
     * @param movieId TMDB电影ID
     * @return JSON响应
     */
    public JsonNode getMovieCredits(int movieId) {
        try {
            String url = String.format("%s/movie/%d/credits?language=zh-CN", baseUrl, movieId);
            HttpEntity<String> entity = new HttpEntity<>(createHeaders());
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                String.class
            );
            
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            log.error("获取电影演职人员失败: movieId={}", movieId, e);
            return null;
        }
    }
    
    /**
     * 获取电影图片
     * @param movieId TMDB电影ID
     * @return JSON响应
     */
    public JsonNode getMovieImages(int movieId) {
        try {
            String url = String.format("%s/movie/%d/images", baseUrl, movieId);
            HttpEntity<String> entity = new HttpEntity<>(createHeaders());
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                String.class
            );
            
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            log.error("获取电影图片失败: movieId={}", movieId, e);
            return null;
        }
    }
    
    /**
     * 构建完整的图片URL
     * @param posterPath 海报路径（如 /abc123.jpg）
     * @param size 图片尺寸（w500, w780, original等）
     * @return 完整的图片URL
     */
    public String getImageUrl(String posterPath, String size) {
        if (posterPath == null || posterPath.isEmpty()) {
            return null;
        }
        return String.format("%s/%s%s", imageBaseUrl, size, posterPath);
    }
    
    /**
     * 获取正在上映的电影
     * @param page 页码
     * @return JSON响应
     */
    public JsonNode getNowPlayingMovies(int page) {
        try {
            String url = String.format("%s/movie/now_playing?language=zh-CN&page=%d&region=CN", 
                baseUrl, page);
            HttpEntity<String> entity = new HttpEntity<>(createHeaders());
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                String.class
            );
            
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            log.error("获取正在上映电影失败", e);
            return null;
        }
    }
    
    /**
     * 获取即将上映的电影
     * @param page 页码
     * @return JSON响应
     */
    public JsonNode getUpcomingMovies(int page) {
        try {
            String url = String.format("%s/movie/upcoming?language=zh-CN&page=%d&region=CN", 
                baseUrl, page);
            HttpEntity<String> entity = new HttpEntity<>(createHeaders());
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                String.class
            );
            
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            log.error("获取即将上映电影失败", e);
            return null;
        }
    }
    
    /**
     * 获取高分电影
     * @param page 页码
     * @return JSON响应
     */
    public JsonNode getTopRatedMovies(int page) {
        try {
            String url = String.format("%s/movie/top_rated?language=zh-CN&page=%d", 
                baseUrl, page);
            HttpEntity<String> entity = new HttpEntity<>(createHeaders());
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                String.class
            );
            
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            log.error("获取高分电影失败", e);
            return null;
        }
    }
}

