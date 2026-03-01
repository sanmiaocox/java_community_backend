package com.community.java_community_backend.service;

import com.community.java_community_backend.dto.request.MarkWatchedRequest;
import com.community.java_community_backend.dto.response.WatchedMovieResponse;
import com.community.java_community_backend.entity.Movie;
import com.community.java_community_backend.entity.User;
import com.community.java_community_backend.entity.WatchedMovie;
import com.community.java_community_backend.repository.MovieRepository;
import com.community.java_community_backend.repository.UserRepository;
import com.community.java_community_backend.repository.WatchedMovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 看过记录服务
 */
@Service
@RequiredArgsConstructor
public class WatchedMovieService {
    
    private final WatchedMovieRepository watchedMovieRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    
    /**
     * 标记电影为看过
     */
    @Transactional
    public WatchedMovieResponse markAsWatched(Long userId, MarkWatchedRequest request) {
        // 验证用户
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 验证电影
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("电影不存在"));
        
        // 检查是否已标记
        if (watchedMovieRepository.existsByUserIdAndMovieId(userId, request.getMovieId())) {
            throw new RuntimeException("该电影已标记为看过");
        }
        
        // 创建看过记录
        WatchedMovie watchedMovie = new WatchedMovie();
        watchedMovie.setUser(user);
        watchedMovie.setMovie(movie);
        watchedMovie.setWatchedAt(LocalDateTime.now());
        watchedMovie.setRating(request.getRating());
        watchedMovie.setNote(request.getNote());
        
        WatchedMovie saved = watchedMovieRepository.save(watchedMovie);
        return convertToResponse(saved);
    }
    
    /**
     * 取消看过标记
     */
    @Transactional
    public void unmarkAsWatched(Long userId, Long movieId) {
        // 检查记录是否存在
        if (!watchedMovieRepository.existsByUserIdAndMovieId(userId, movieId)) {
            throw new RuntimeException("看过记录不存在");
        }
        
        watchedMovieRepository.deleteByUserIdAndMovieId(userId, movieId);
    }
    
    /**
     * 更新看过记录
     */
    @Transactional
    public WatchedMovieResponse updateWatchedMovie(Long userId, Long movieId, MarkWatchedRequest request) {
        WatchedMovie watchedMovie = watchedMovieRepository.findByUserIdAndMovieId(userId, movieId)
                .orElseThrow(() -> new RuntimeException("看过记录不存在"));
        
        if (request.getRating() != null) {
            watchedMovie.setRating(request.getRating());
        }
        if (request.getNote() != null) {
            watchedMovie.setNote(request.getNote());
        }
        
        WatchedMovie updated = watchedMovieRepository.save(watchedMovie);
        return convertToResponse(updated);
    }
    
    /**
     * 获取用户看过的所有电影
     */
    public List<WatchedMovieResponse> getUserWatchedMovies(Long userId) {
        List<WatchedMovie> watchedMovies = watchedMovieRepository.findByUserIdOrderByWatchedAtDesc(userId);
        return watchedMovies.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 检查用户是否看过某部电影
     */
    public boolean isWatched(Long userId, Long movieId) {
        return watchedMovieRepository.existsByUserIdAndMovieId(userId, movieId);
    }
    
    /**
     * 获取用户看过的电影数量
     */
    public long getUserWatchedCount(Long userId) {
        return watchedMovieRepository.countByUserId(userId);
    }
    
    /**
     * 转换为响应DTO
     */
    private WatchedMovieResponse convertToResponse(WatchedMovie watchedMovie) {
        Movie movie = watchedMovie.getMovie();
        
        WatchedMovieResponse.MovieSimpleInfo movieInfo = WatchedMovieResponse.MovieSimpleInfo.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .posterUrl(movie.getPosterUrl())
                .rating(movie.getRating())
                .year(movie.getYear())
                .build();
        
        return WatchedMovieResponse.builder()
                .id(watchedMovie.getId())
                .userId(watchedMovie.getUser().getId())
                .movieId(movie.getId())
                .watchedAt(watchedMovie.getWatchedAt())
                .rating(watchedMovie.getRating())
                .note(watchedMovie.getNote())
                .createdAt(watchedMovie.getCreatedAt())
                .updatedAt(watchedMovie.getUpdatedAt())
                .movieInfo(movieInfo)
                .build();
    }
}

