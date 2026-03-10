package com.community.java_community_backend.service;

import com.community.java_community_backend.dto.request.CreateCommentRequest;
import com.community.java_community_backend.dto.response.CommentResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 评论服务
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final NotificationService notificationService;
    
    /**
     * 发表评论
     */
    @Transactional
    public CommentResponse createComment(Long userId, Long feedId, CreateCommentRequest request) {
        // 检查动态是否存在
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("动态不存在"));
        
        // 查询用户
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 创建评论
        Comment comment = new Comment();
        comment.setFeed(feed);
        comment.setUser(user);
        comment.setContent(request.getContent());
        comment = commentRepository.save(comment);
        
        // 更新动态评论数
        feed.setCommentCount(feed.getCommentCount() + 1);
        feedRepository.save(feed);

        // 触发通知（非本人动态）
        String preview = request.getContent().length() > 50
                ? request.getContent().substring(0, 50) + "..."
                : request.getContent();
        notificationService.createNotification(
                feed.getUser().getId(), userId,
                Notification.NotificationType.COMMENT_FEED,
                "FEED", feedId, preview);
        
        return convertToResponse(comment, userId);
    }
    
    /**
     * 获取动态的评论列表
     */
    @Transactional(readOnly = true)
    public Page<CommentResponse> getFeedComments(Long feedId, Long currentUserId, Pageable pageable) {
        // 检查动态是否存在
        if (!feedRepository.existsById(feedId)) {
            throw new RuntimeException("动态不存在");
        }
        
        Page<Comment> comments = commentRepository.findByFeedIdOrderByCreatedAtDesc(feedId, pageable);
        
        // 批量查询点赞状态
        List<Long> commentIds = comments.getContent().stream()
                .map(Comment::getId)
                .collect(Collectors.toList());
        
        Map<Long, Boolean> likeStatusMap = batchCheckLikeStatus(currentUserId, commentIds);
        
        return comments.map(comment -> convertToResponse(comment, currentUserId, likeStatusMap));
    }
    
    /**
     * 删除评论
     */
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("评论不存在"));
        
        // 检查权限
        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("只能删除自己的评论");
        }
        
        // 获取关联的动态
        Feed feed = comment.getFeed();
        
        // 删除评论
        commentRepository.delete(comment);
        
        // 更新动态评论数
        feed.setCommentCount(Math.max(0, feed.getCommentCount() - 1));
        feedRepository.save(feed);
    }
    
    /**
     * 批量查询点赞状态
     */
    private Map<Long, Boolean> batchCheckLikeStatus(Long userId, List<Long> commentIds) {
        if (commentIds.isEmpty()) {
            return Collections.emptyMap();
        }
        
        List<Like> likes = likeRepository.findByUserIdAndTargetTypeAndTargetIdIn(
                userId, Like.TargetType.COMMENT, commentIds);
        
        return likes.stream()
                .collect(Collectors.toMap(Like::getTargetId, like -> true));
    }
    
    /**
     * 转换为响应（带点赞状态缓存）
     */
    private CommentResponse convertToResponse(Comment comment, Long currentUserId, Map<Long, Boolean> likeStatusMap) {
        CommentResponse response = convertToResponse(comment, currentUserId);
        
        // 使用缓存的点赞状态
        if (likeStatusMap != null) {
            response.setIsLiked(likeStatusMap.getOrDefault(comment.getId(), false));
        }
        
        return response;
    }
    
    /**
     * 转换为响应
     */
    private CommentResponse convertToResponse(Comment comment, Long currentUserId) {
        // 构建用户信息
        CommentResponse.UserSimpleDTO userDTO = CommentResponse.UserSimpleDTO.builder()
                .id(comment.getUser().getId())
                .userCode(comment.getUser().getUserCode())
                .username(comment.getUser().getUsername())
                .avatar(comment.getUser().getAvatar())
                .build();
        
        // 查询当前用户是否点赞
        boolean isLiked = likeRepository.existsByUserIdAndTargetTypeAndTargetId(
                currentUserId, Like.TargetType.COMMENT, comment.getId());
        
        return CommentResponse.builder()
                .id(comment.getId())
                .user(userDTO)
                .content(comment.getContent())
                .likeCount(comment.getLikeCount())
                .isLiked(isLiked)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}



