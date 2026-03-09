package com.community.java_community_backend.service;

import com.community.java_community_backend.dto.request.CreateFeedRequest;
import com.community.java_community_backend.dto.response.FeedResponse;
import com.community.java_community_backend.entity.*;
import com.community.java_community_backend.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 动态服务
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FeedService {
    
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final EventRepository eventRepository;
    private final LikeRepository likeRepository;
    private final FollowRepository followRepository;
    private final ObjectMapper objectMapper;
    
    /**
     * 发布动态
     */
    @Transactional
    public FeedResponse createFeed(Long userId, CreateFeedRequest request) {
        // 查询用户
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 创建动态
        Feed feed = new Feed();
        feed.setUser(user);
        feed.setContent(request.getContent());
        
        // 处理图片（转换为JSON字符串）
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            try {
                feed.setImages(objectMapper.writeValueAsString(request.getImages()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("图片数据格式错误");
            }
        }
        
        // 关联电影
        if (request.getMovieId() != null) {
            Movie movie = movieRepository.findById(request.getMovieId())
                    .orElseThrow(() -> new RuntimeException("电影不存在"));
            feed.setMovie(movie);
        }
        
        // 关联活动
        if (request.getEventId() != null) {
            Event event = eventRepository.findById(request.getEventId())
                    .orElseThrow(() -> new RuntimeException("活动不存在"));
            feed.setEvent(event);
        }
        
        // 保存动态
        feed = feedRepository.save(feed);
        
        // 返回响应
        return convertToResponse(feed, userId);
    }
    
    /**
     * 获取动态列表（关注人的动态）
     */
    @Transactional(readOnly = true)
    public Page<FeedResponse> getFollowingFeeds(Long userId, Pageable pageable) {
        // 获取关注的人的ID列表
        List<Long> followingIds = followRepository.findFollowingIdsByFollowerId(userId);
        
        // 如果没有关注任何人，返回空列表
        if (followingIds.isEmpty()) {
            return Page.empty(pageable);
        }
        
        // 查询关注人的动态
        Page<Feed> feeds = feedRepository.findByUserIdIn(followingIds, pageable);
        
        // 批量查询点赞状态
        List<Long> feedIds = feeds.getContent().stream()
                .map(Feed::getId)
                .collect(Collectors.toList());
        
        Map<Long, Boolean> likeStatusMap = batchCheckLikeStatus(userId, Like.TargetType.FEED, feedIds);
        
        // 转换为响应
        return feeds.map(feed -> convertToResponse(feed, userId, likeStatusMap));
    }
    
    /**
     * 获取用户动态列表
     */
    @Transactional(readOnly = true)
    public Page<FeedResponse> getUserFeeds(Long targetUserId, Long currentUserId, Pageable pageable) {
        Page<Feed> feeds = feedRepository.findByUserIdOrderByCreatedAtDesc(targetUserId, pageable);
        
        // 批量查询点赞状态
        List<Long> feedIds = feeds.getContent().stream()
                .map(Feed::getId)
                .collect(Collectors.toList());
        
        Map<Long, Boolean> likeStatusMap = batchCheckLikeStatus(currentUserId, Like.TargetType.FEED, feedIds);
        
        return feeds.map(feed -> convertToResponse(feed, currentUserId, likeStatusMap));
    }
    
    /**
     * 获取电影相关动态
     */
    @Transactional(readOnly = true)
    public Page<FeedResponse> getMovieFeeds(Long movieId, Long currentUserId, Pageable pageable) {
        Page<Feed> feeds = feedRepository.findByMovieIdOrderByCreatedAtDesc(movieId, pageable);
        
        // 批量查询点赞状态
        List<Long> feedIds = feeds.getContent().stream()
                .map(Feed::getId)
                .collect(Collectors.toList());
        
        Map<Long, Boolean> likeStatusMap = batchCheckLikeStatus(currentUserId, Like.TargetType.FEED, feedIds);
        
        return feeds.map(feed -> convertToResponse(feed, currentUserId, likeStatusMap));
    }
    
    /**
     * 获取动态详情
     */
    @Transactional(readOnly = true)
    public FeedResponse getFeedById(Long feedId, Long currentUserId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("动态不存在"));
        
        return convertToResponse(feed, currentUserId);
    }
    
    /**
     * 删除动态
     */
    @Transactional
    public void deleteFeed(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("动态不存在"));
        
        // 检查权限
        if (!feed.getUser().getId().equals(userId)) {
            throw new RuntimeException("只能删除自己的动态");
        }
        
        // 删除动态（会级联删除评论和点赞）
        feedRepository.delete(feed);
    }
    
    /**
     * 批量查询点赞状态
     */
    private Map<Long, Boolean> batchCheckLikeStatus(Long userId, Like.TargetType targetType, List<Long> targetIds) {
        if (targetIds.isEmpty()) {
            return Collections.emptyMap();
        }
        
        List<Like> likes = likeRepository.findByUserIdAndTargetTypeAndTargetIdIn(userId, targetType, targetIds);
        
        return likes.stream()
                .collect(Collectors.toMap(Like::getTargetId, like -> true));
    }
    
    /**
     * 转换为响应（带点赞状态缓存）
     */
    private FeedResponse convertToResponse(Feed feed, Long currentUserId, Map<Long, Boolean> likeStatusMap) {
        FeedResponse response = convertToResponse(feed, currentUserId);
        
        // 使用缓存的点赞状态
        if (likeStatusMap != null) {
            response.setIsLiked(likeStatusMap.getOrDefault(feed.getId(), false));
        }
        
        return response;
    }
    
    /**
     * 转换为响应
     */
    private FeedResponse convertToResponse(Feed feed, Long currentUserId) {
        // 解析图片JSON
        List<String> images = null;
        if (feed.getImages() != null && !feed.getImages().isEmpty()) {
            try {
                images = objectMapper.readValue(feed.getImages(), new TypeReference<List<String>>() {});
            } catch (JsonProcessingException e) {
                log.error("解析图片JSON失败: {}", e.getMessage());
            }
        }
        
        // 构建用户信息
        FeedResponse.UserSimpleDTO userDTO = FeedResponse.UserSimpleDTO.builder()
                .id(feed.getUser().getId())
                .userCode(feed.getUser().getUserCode())
                .username(feed.getUser().getUsername())
                .avatar(feed.getUser().getAvatar())
                .build();
        
        // 构建电影信息
        FeedResponse.MovieSimpleDTO movieDTO = null;
        if (feed.getMovie() != null) {
            movieDTO = FeedResponse.MovieSimpleDTO.builder()
                    .id(feed.getMovie().getId())
                    .tmdbId(feed.getMovie().getTmdbId())
                    .title(feed.getMovie().getTitle())
                    .posterUrl(feed.getMovie().getPosterUrl())
                    .rating(feed.getMovie().getRating())
                    .build();
        }
        
        // 构建活动信息
        FeedResponse.EventSimpleDTO eventDTO = null;
        if (feed.getEvent() != null) {
            eventDTO = FeedResponse.EventSimpleDTO.builder()
                    .id(feed.getEvent().getId())
                    .title(feed.getEvent().getTitle())
                    .imageUrl(feed.getEvent().getImageUrl())
                    .eventDate(feed.getEvent().getEventDate())
                    .build();
        }
        
        // 查询当前用户是否点赞
        boolean isLiked = likeRepository.existsByUserIdAndTargetTypeAndTargetId(
                currentUserId, Like.TargetType.FEED, feed.getId());
        
        return FeedResponse.builder()
                .id(feed.getId())
                .user(userDTO)
                .movie(movieDTO)
                .event(eventDTO)
                .content(feed.getContent())
                .images(images)
                .likeCount(feed.getLikeCount())
                .commentCount(feed.getCommentCount())
                .isLiked(isLiked)
                .createdAt(feed.getCreatedAt())
                .updatedAt(feed.getUpdatedAt())
                .build();
    }
}



