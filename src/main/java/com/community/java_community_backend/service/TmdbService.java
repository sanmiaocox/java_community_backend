package com.community.java_community_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * TMDB API服务
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TmdbService {
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Value("${tmdb.api.read-token}")
    private String readToken;
    
    @Value("${tmdb.api.base-url:https://api.themoviedb.org/3}")
    private String baseUrl;
    
    @Value("${tmdb.image.base-url}")
    private String imageBaseUrl;
    
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
     * 执行TMDB API请求
     */
    private JsonNode executeRequest(String url) {
        try {
            HttpEntity<String> entity = new HttpEntity<>(createHeaders());
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class
            );
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            log.error("TMDB请求失败: {}", e.getMessage());
            return null;
        }
    }
    
    public JsonNode getPopularMovies(int page) {
        String url = String.format("%s/movie/popular?language=zh-CN&page=%d", baseUrl, page);
        return executeRequest(url);
    }
    
    public JsonNode getMovieDetails(int movieId) {
        String url = String.format("%s/movie/%d?language=zh-CN", baseUrl, movieId);
        return executeRequest(url);
    }
    
    public JsonNode searchMovies(String keyword, int page) {
        String url = String.format("%s/search/movie?query=%s&language=zh-CN&page=%d", 
            baseUrl, keyword, page);
        return executeRequest(url);
    }
    
    public JsonNode getMovieCredits(int movieId) {
        String url = String.format("%s/movie/%d/credits?language=zh-CN", baseUrl, movieId);
        return executeRequest(url);
    }
    
    public JsonNode getMovieImages(int movieId) {
        String url = String.format("%s/movie/%d/images", baseUrl, movieId);
        return executeRequest(url);
    }
    
    public String getImageUrl(String posterPath, String size) {
        if (posterPath == null || posterPath.isEmpty()) {
            return null;
        }
        return String.format("%s/%s%s", imageBaseUrl, size, posterPath);
    }
    
    public JsonNode getNowPlayingMovies(int page) {
        String url = String.format("%s/movie/now_playing?language=zh-CN&page=%d&region=CN", 
            baseUrl, page);
        return executeRequest(url);
    }
    
    public JsonNode getUpcomingMovies(int page) {
        String url = String.format("%s/movie/upcoming?language=zh-CN&page=%d&region=CN", 
            baseUrl, page);
        return executeRequest(url);
    }
    
    public JsonNode getTopRatedMovies(int page) {
        String url = String.format("%s/movie/top_rated?language=zh-CN&page=%d", 
            baseUrl, page);
        return executeRequest(url);
    }
    
    public JsonNode getMovieRecommendations(int movieId, int page) {
        String url = String.format("%s/movie/%d/recommendations?language=zh-CN&page=%d", 
            baseUrl, movieId, page);
        return executeRequest(url);
    }
    
    /**
     * 发现电影（高级筛选）
     * @param language 语言代码
     * @param sortBy 排序方式，如 popularity.desc
     * @param withGenres 类型ID，如 28,12（动作+冒险）
     * @param primaryReleaseYear 上映年份
     * @param voteAverageGte 最低评分
     * @param voteAverageLte 最高评分
     * @param page 页码
     */
    public JsonNode discoverMovies(String language, String sortBy, String withGenres, 
                                   Integer primaryReleaseYear, Double voteAverageGte, 
                                   Double voteAverageLte, int page) {
        StringBuilder urlBuilder = new StringBuilder(String.format("%s/discover/movie?page=%d", baseUrl, page));
        
        // 添加语言参数，默认中文
        if (language != null && !language.isEmpty()) {
            urlBuilder.append("&language=").append(language);
        } else {
            urlBuilder.append("&language=zh-CN");
        }
        
        // 添加排序参数
        if (sortBy != null && !sortBy.isEmpty()) {
            urlBuilder.append("&sort_by=").append(sortBy);
        }
        
        // 添加类型筛选
        if (withGenres != null && !withGenres.isEmpty()) {
            urlBuilder.append("&with_genres=").append(withGenres);
        }
        
        // 添加上映年份
        if (primaryReleaseYear != null) {
            urlBuilder.append("&primary_release_year=").append(primaryReleaseYear);
        }
        
        // 添加最低评分
        if (voteAverageGte != null) {
            urlBuilder.append("&vote_average.gte=").append(voteAverageGte);
        }
        
        // 添加最高评分
        if (voteAverageLte != null) {
            urlBuilder.append("&vote_average.lte=").append(voteAverageLte);
        }
        
        return executeRequest(urlBuilder.toString());
    }
    
    /**
     * 获取电影类型列表
     * @param language 语言代码
     */
    public JsonNode getMovieGenres(String language) {
        String url = String.format("%s/genre/movie/list?language=%s", 
            baseUrl, language != null && !language.isEmpty() ? language : "zh-CN");
        return executeRequest(url);
    }
}

