package com.community.java_community_backend.service;

import com.community.java_community_backend.dto.response.FollowStatusResponse;
import com.community.java_community_backend.dto.response.UserInfoResponse;
import com.community.java_community_backend.entity.Follow;
import com.community.java_community_backend.entity.User;
import com.community.java_community_backend.exception.BusinessException;
import com.community.java_community_backend.repository.FollowRepository;
import com.community.java_community_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 关注服务类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {
    
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    
    /**
     * 关注用户
     */
    @Transactional
    public void followUser(Long followerId, Long followingId) {
        // 不能关注自己
        if (followerId.equals(followingId)) {
            throw new BusinessException(6001, "不能关注自己");
        }
        
        // 检查被关注用户是否存在
        if (!userRepository.existsById(followingId)) {
            throw new BusinessException(404, "用户不存在");
        }
        
        // 检查是否已关注
        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new BusinessException(6002, "已经关注过了");
        }
        
        // 创建关注关系
        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFollowingId(followingId);
        followRepository.save(follow);
        
        log.info("用户{}关注了用户{}", followerId, followingId);
    }
    
    /**
     * 取消关注
     */
    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        // 检查是否已关注
        if (!followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new BusinessException(6003, "还未关注");
        }
        
        // 删除关注关系
        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
        
        log.info("用户{}取消关注了用户{}", followerId, followingId);
    }
    
    /**
     * 获取关注列表（我关注的人）
     */
    public Page<UserInfoResponse> getFollowingList(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Follow> follows = followRepository.findByFollowerId(userId, pageable);
        
        return follows.map(follow -> {
            User user = userRepository.findById(follow.getFollowingId())
                    .orElseThrow(() -> new BusinessException(404, "用户不存在"));
            return UserInfoResponse.fromEntity(user);
        });
    }
    
    /**
     * 获取粉丝列表（关注我的人）
     */
    public Page<UserInfoResponse> getFollowerList(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Follow> follows = followRepository.findByFollowingId(userId, pageable);
        
        return follows.map(follow -> {
            User user = userRepository.findById(follow.getFollowerId())
                    .orElseThrow(() -> new BusinessException(404, "用户不存在"));
            return UserInfoResponse.fromEntity(user);
        });
    }
    
    /**
     * 获取好友列表（互相关注）
     */
    public List<UserInfoResponse> getFriendList(Long userId) {
        List<Long> friendIds = followRepository.findFriendIds(userId);
        
        return friendIds.stream()
                .map(friendId -> userRepository.findById(friendId)
                        .orElseThrow(() -> new BusinessException(404, "用户不存在")))
                .map(UserInfoResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取关注状态
     */
    public FollowStatusResponse getFollowStatus(Long currentUserId, Long targetUserId) {
        boolean isFollowing = followRepository.existsByFollowerIdAndFollowingId(currentUserId, targetUserId);
        boolean isFollower = followRepository.existsByFollowerIdAndFollowingId(targetUserId, currentUserId);
        boolean isFriend = isFollowing && isFollower;
        
        return new FollowStatusResponse(isFollowing, isFollower, isFriend);
    }
    
    /**
     * 统计关注数
     */
    public long countFollowing(Long userId) {
        return followRepository.countByFollowerId(userId);
    }
    
    /**
     * 统计粉丝数
     */
    public long countFollowers(Long userId) {
        return followRepository.countByFollowingId(userId);
    }
    
    /**
     * 统计好友数
     */
    public long countFriends(Long userId) {
        return followRepository.countFriends(userId);
    }
}

