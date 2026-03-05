package com.community.java_community_backend.controller;

import com.community.java_community_backend.dto.request.SaveMovieRequest;
import com.community.java_community_backend.dto.response.ApiResponse;
import com.community.java_community_backend.dto.response.MovieResponse;
import com.community.java_community_backend.entity.Movie;
import com.community.java_community_backend.service.MovieService;
import com.community.java_community_backend.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 电影控制器
 */
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    
    private final MovieService movieService;
    private final JwtUtil jwtUtil;
    
    /**
     * 保存电影到数据库
     * 从TMDB获取电影信息并保存到本地数据库
     * 如果电影已存在（根据tmdbId判断），则直接返回已存在的电影信息
     */
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<MovieResponse>> saveMovie(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody SaveMovieRequest request) {
        // 验证用户身份
        jwtUtil.extractUserId(token.replace("Bearer ", ""));
        
        // 检查电影是否已存在
        if (movieService.existsByTmdbId(request.getTmdbId())) {
            Movie existingMovie = movieService.getLocalMovieByTmdbId(request.getTmdbId())
                    .orElseThrow(() -> new RuntimeException("电影数据异常"));
            MovieResponse response = convertToResponse(existingMovie);
            return ResponseEntity.ok(ApiResponse.success(response));
        }
        
        // 保存电影
        Movie movie = movieService.saveOrUpdateMovie(request.getTmdbId());
        MovieResponse response = convertToResponse(movie);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * 获取本地电影详情
     */
    @GetMapping("/{movieId}")
    public ResponseEntity<ApiResponse<MovieResponse>> getMovie(
            @RequestHeader("Authorization") String token,
            @PathVariable Long movieId) {
        // 验证用户身份
        jwtUtil.extractUserId(token.replace("Bearer ", ""));
        
        Movie movie = movieService.getLocalMovie(movieId);
        MovieResponse response = convertToResponse(movie);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * 根据TMDB ID获取本地电影详情
     */
    @GetMapping("/tmdb/{tmdbId}")
    public ResponseEntity<ApiResponse<MovieResponse>> getMovieByTmdbId(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer tmdbId) {
        // 验证用户身份
        jwtUtil.extractUserId(token.replace("Bearer ", ""));
        
        Movie movie = movieService.getLocalMovieByTmdbId(tmdbId)
                .orElseThrow(() -> new RuntimeException("电影不存在"));
        MovieResponse response = convertToResponse(movie);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * 获取本地电影列表（分页）
     */
    @GetMapping
    public ResponseEntity<ApiResponse<org.springframework.data.domain.Page<MovieResponse>>> getMovies(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // 验证用户身份
        jwtUtil.extractUserId(token.replace("Bearer ", ""));
        
        org.springframework.data.domain.Page<Movie> movies = movieService.getLocalMovies(page, size);
        org.springframework.data.domain.Page<MovieResponse> response = movies.map(this::convertToResponse);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * 搜索本地电影
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<org.springframework.data.domain.Page<MovieResponse>>> searchMovies(
            @RequestHeader("Authorization") String token,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // 验证用户身份
        jwtUtil.extractUserId(token.replace("Bearer ", ""));
        
        org.springframework.data.domain.Page<Movie> movies = movieService.searchMovies(keyword, page, size);
        org.springframework.data.domain.Page<MovieResponse> response = movies.map(this::convertToResponse);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * 转换为响应DTO
     */
    private MovieResponse convertToResponse(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .tmdbId(movie.getTmdbId())
                .title(movie.getTitle())
                .originalTitle(movie.getOriginalTitle())
                .posterUrl(movie.getPosterUrl())
                .rating(movie.getRating())
                .ratingSource(movie.getRatingSource())
                .releaseDate(movie.getReleaseDate())
                .year(movie.getYear())
                .genres(movie.getGenres())
                .genre(movie.getGenre())
                .region(movie.getRegion())
                .languages(movie.getLanguages())
                .directors(movie.getDirectors())
                .actors(movie.getActors())
                .synopsis(movie.getSynopsis())
                .tmdbUrl(movie.getTmdbUrl())
                .createdAt(movie.getCreatedAt())
                .updatedAt(movie.getUpdatedAt())
                .build();
    }
}


