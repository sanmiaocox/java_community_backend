package com.community.java_community_backend.service;

import com.community.java_community_backend.entity.Movie;
import com.community.java_community_backend.repository.MovieRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 电影服务
 * 负责本地电影数据管理和TMDB数据转换
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {
    
    private final MovieRepository movieRepository;
    private final TmdbService tmdbService;
    
    /**
     * 保存或更新电影到本地数据库
     * 当用户收藏、标记看过、发布动态时调用
     * 
     * @param tmdbId TMDB电影ID
     * @return 本地电影实体
     */
    @Transactional
    public Movie saveOrUpdateMovie(Integer tmdbId) {
        // 检查本地是否已存在
        Optional<Movie> existing = movieRepository.findByTmdbId(tmdbId);
        if (existing.isPresent()) {
            log.debug("电影已存在于本地数据库: tmdbId={}, localId={}", tmdbId, existing.get().getId());
            return existing.get();
        }
        
        // 从TMDB获取详情
        JsonNode tmdbData = tmdbService.getMovieDetails(tmdbId);
        if (tmdbData == null) {
            throw new RuntimeException("无法从TMDB获取电影信息: tmdbId=" + tmdbId);
        }
        
        // 转换并保存到本地
        Movie movie = convertTmdbToMovie(tmdbData);
        Movie saved = movieRepository.save(movie);
        log.info("电影已保存到本地数据库: tmdbId={}, localId={}, title={}", 
            tmdbId, saved.getId(), saved.getTitle());
        
        return saved;
    }
    
    /**
     * 将TMDB数据转换为本地Movie实体
     * 
     * @param tmdbData TMDB API返回的JSON数据
     * @return Movie实体
     */
    private Movie convertTmdbToMovie(JsonNode tmdbData) {
        Movie movie = new Movie();
        
        // 基本信息
        movie.setTmdbId(tmdbData.get("id").asInt());
        movie.setTitle(tmdbData.has("title") ? tmdbData.get("title").asText() : null);
        movie.setOriginalTitle(tmdbData.has("original_title") ? tmdbData.get("original_title").asText() : null);
        movie.setSynopsis(tmdbData.has("overview") ? tmdbData.get("overview").asText() : null);
        
        // 海报
        if (tmdbData.has("poster_path") && !tmdbData.get("poster_path").isNull()) {
            String posterPath = tmdbData.get("poster_path").asText();
            movie.setPosterUrl(tmdbService.getImageUrl(posterPath, "w500"));
        }
        
        // 评分
        if (tmdbData.has("vote_average")) {
            movie.setRating(tmdbData.get("vote_average").asDouble());
            movie.setRatingSource("TMDB");
        }
        
        // 上映日期
        if (tmdbData.has("release_date")) {
            movie.setReleaseDate(tmdbData.get("release_date").asText());
            // 提取年份
            String releaseDate = tmdbData.get("release_date").asText();
            if (releaseDate != null && releaseDate.length() >= 4) {
                movie.setYear(releaseDate.substring(0, 4));
            }
        }
        
        // 类型
        if (tmdbData.has("genres") && tmdbData.get("genres").isArray()) {
            StringBuilder genresBuilder = new StringBuilder();
            tmdbData.get("genres").forEach(genre -> {
                if (genresBuilder.length() > 0) {
                    genresBuilder.append(",");
                }
                genresBuilder.append(genre.get("name").asText());
            });
            movie.setGenres(genresBuilder.toString());
            
            // 设置主要类型（第一个类型）
            if (tmdbData.get("genres").size() > 0) {
                movie.setGenre(tmdbData.get("genres").get(0).get("name").asText());
            }
        }
        
        // 地区
        if (tmdbData.has("production_countries") && tmdbData.get("production_countries").isArray()) {
            StringBuilder regionBuilder = new StringBuilder();
            tmdbData.get("production_countries").forEach(country -> {
                if (regionBuilder.length() > 0) {
                    regionBuilder.append(",");
                }
                regionBuilder.append(country.get("name").asText());
            });
            movie.setRegion(regionBuilder.toString());
        }
        
        // 语言
        if (tmdbData.has("spoken_languages") && tmdbData.get("spoken_languages").isArray()) {
            StringBuilder languagesBuilder = new StringBuilder();
            tmdbData.get("spoken_languages").forEach(language -> {
                if (languagesBuilder.length() > 0) {
                    languagesBuilder.append(",");
                }
                languagesBuilder.append(language.get("name").asText());
            });
            movie.setLanguages(languagesBuilder.toString());
        }
        
        // TMDB链接
        movie.setTmdbUrl("https://www.themoviedb.org/movie/" + tmdbData.get("id").asInt());
        
        return movie;
    }
    
    /**
     * 根据TMDB ID获取本地电影
     * 
     * @param tmdbId TMDB电影ID
     * @return 本地电影实体（如果存在）
     */
    public Optional<Movie> getLocalMovieByTmdbId(Integer tmdbId) {
        return movieRepository.findByTmdbId(tmdbId);
    }
    
    /**
     * 获取本地电影列表（分页）
     * 
     * @param page 页码
     * @param size 每页数量
     * @return 电影分页数据
     */
    public Page<Movie> getLocalMovies(int page, int size) {
        return movieRepository.findAll(PageRequest.of(page, size));
    }
    
    /**
     * 根据本地ID获取电影
     * 
     * @param movieId 本地电影ID
     * @return 电影实体
     */
    public Movie getLocalMovie(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("电影不存在: id=" + movieId));
    }
    
    /**
     * 检查电影是否已存在于本地
     * 
     * @param tmdbId TMDB电影ID
     * @return 是否存在
     */
    public boolean existsByTmdbId(Integer tmdbId) {
        return movieRepository.existsByTmdbId(tmdbId);
    }
    
    /**
     * 搜索本地电影
     * 
     * @param keyword 搜索关键词
     * @param page 页码
     * @param size 每页数量
     * @return 电影分页数据
     */
    public Page<Movie> searchMovies(String keyword, int page, int size) {
        return movieRepository.searchMovies(keyword, PageRequest.of(page, size));
    }
}

