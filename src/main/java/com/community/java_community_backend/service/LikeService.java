package com.community.java_community_backend.service;

import com.community.java_community_backend.dto.response.LikeStatusResponse;
import com.community.java_community_backend.entity.Comment;
import com.community.java_community_backend.entity.Feed;
import com.community.java_community_backend.entity.Like;
import com.community.java_community_backend.entity.Notification;
import com.community.java_community_backend.entity.User;
import com.community.java_community_backend.repository.CommentRepository;
import com.community.java_community_backend.repository.FeedRepository;
import com.community.java_community_backend.repository.LikeRepository;
import com.community.java_community_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 点赞服务
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {
    
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;
    
    /**
     * 点赞动态
     */
    @Transactional
    public LikeStatusResponse likeFeed(Long userId, Long feedId) {
        // 检查动态是否存在
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("动态不存在"));
        
        // 检查是否已点赞
        if (likeRepository.existsByUserIdAndTargetTypeAndTargetId(userId, Like.TargetType.FEED, feedId)) {
            throw new RuntimeException("已经点赞过了");
        }
        
        // 查询用户
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 创建点赞记录
        Like like = new Like();
        like.setUser(user);
        like.setTargetType(Like.TargetType.FEED);
        like.setTargetId(feedId);
        likeRepository.save(like);
        
        // 更新动态点赞数
        feed.setLikeCount(feed.getLikeCount() + 1);
        feedRepository.save(feed);

        // 触发通知（非本人动态）
        notificationService.createNotification(
                feed.getUser().getId(), userId,
                Notification.NotificationType.LIKE_FEED,
                "FEED", feedId, null);
        
        return LikeStatusResponse.builder()
                .isLiked(true)
                .likeCount(feed.getLikeCount())
                .build();
    }
    
    /**
     * 取消点赞动态
     */
    @Transactional
    public LikeStatusResponse unlikeFeed(Long userId, Long feedId) {
        // 检查动态是否存在
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("动态不存在"));
        
        // 检查是否已点赞
        if (!likeRepository.existsByUserIdAndTargetTypeAndTargetId(userId, Like.TargetType.FEED, feedId)) {
            throw new RuntimeException("还未点赞");
        }
        
        // 删除点赞记录
        likeRepository.deleteByUserIdAndTargetTypeAndTargetId(userId, Like.TargetType.FEED, feedId);
        
        // 更新动态点赞数
        feed.setLikeCount(Math.max(0, feed.getLikeCount() - 1));
        feedRepository.save(feed);
        
        return LikeStatusResponse.builder()
                .isLiked(false)
                .likeCount(feed.getLikeCount())
                .build();
    }
    
    /**
     * 检查是否点赞动态
     */
    @Transactional(readOnly = true)
    public LikeStatusResponse checkFeedLikeStatus(Long userId, Long feedId) {
        // 检查动态是否存在
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("动态不存在"));
        
        boolean isLiked = likeRepository.existsByUserIdAndTargetTypeAndTargetId(
                userId, Like.TargetType.FEED, feedId);
        
        return LikeStatusResponse.builder()
                .isLiked(isLiked)
                .likeCount(feed.getLikeCount())
                .build();
    }
    
    /**
     * 点赞评论
     */
    @Transactional
    public LikeStatusResponse likeComment(Long userId, Long commentId) {
        // 检查评论是否存在
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("评论不存在"));
        
        // 检查是否已点赞
        if (likeRepository.existsByUserIdAndTargetTypeAndTargetId(userId, Like.TargetType.COMMENT, commentId)) {
            throw new RuntimeException("已经点赞过了");
        }
        
        // 查询用户
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 创建点赞记录
        Like like = new Like();
        like.setUser(user);
        like.setTargetType(Like.TargetType.COMMENT);
        like.setTargetId(commentId);
        likeRepository.save(like);
        
        // 更新评论点赞数
        comment.setLikeCount(comment.getLikeCount() + 1);
        commentRepository.save(comment);

        // 触发通知（非本人评论）
        notificationService.createNotification(
                comment.getUser().getId(), userId,
                Notification.NotificationType.LIKE_COMMENT,
                "COMMENT", commentId, null);
        
        return LikeStatusResponse.builder()
                .isLiked(true)
                .likeCount(comment.getLikeCount())
                .build();
    }
    
    /**
     * 取消点赞评论
     */
    @Transactional
    public LikeStatusResponse unlikeComment(Long userId, Long commentId) {
        // 检查评论是否存在
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("评论不存在"));
        
        // 检查是否已点赞
        if (!likeRepository.existsByUserIdAndTargetTypeAndTargetId(userId, Like.TargetType.COMMENT, commentId)) {
            throw new RuntimeException("还未点赞");
        }
        
        // 删除点赞记录
        likeRepository.deleteByUserIdAndTargetTypeAndTargetId(userId, Like.TargetType.COMMENT, commentId);
        
        // 更新评论点赞数
        comment.setLikeCount(Math.max(0, comment.getLikeCount() - 1));
        commentRepository.save(comment);
        
        return LikeStatusResponse.builder()
                .isLiked(false)
                .likeCount(comment.getLikeCount())
                .build();
    }
    
    /**
     * 检查是否点赞评论
     */
    @Transactional(readOnly = true)
    public LikeStatusResponse checkCommentLikeStatus(Long userId, Long commentId) {
        // 检查评论是否存在
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("评论不存在"));
        
        boolean isLiked = likeRepository.existsByUserIdAndTargetTypeAndTargetId(
                userId, Like.TargetType.COMMENT, commentId);
        
        return LikeStatusResponse.builder()
                .isLiked(isLiked)
                .likeCount(comment.getLikeCount())
                .build();
    }
}



