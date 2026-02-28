package com.community.java_community_backend.service;

import com.community.java_community_backend.dto.request.UpdateProfileRequest;
import com.community.java_community_backend.dto.response.UserInfoResponse;
import com.community.java_community_backend.entity.User;
import com.community.java_community_backend.exception.BusinessException;
import com.community.java_community_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务类
 * 负责用户相关的业务逻辑
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    
    /**
     * 获取用户信息
     */
    public UserInfoResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));
        return UserInfoResponse.fromEntity(user);
    }
    
    /**
     * 更新个人资料
     */
    @Transactional
    public UserInfoResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));
        
        // 更新用户名（如果提供且不同）
        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            // 检查用户名是否已被占用
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new BusinessException(1001, "用户名已存在");
            }
            user.setUsername(request.getUsername());
            log.info("用户{}更新用户名: {} -> {}", userId, user.getUsername(), request.getUsername());
        }
        
        // 更新头像（如果提供）
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
            log.info("用户{}更新头像", userId);
        }
        
        // 更新个人简介（如果提供）
        if (request.getBio() != null) {
            user.setBio(request.getBio());
            log.info("用户{}更新个人简介", userId);
        }
        
        User savedUser = userRepository.save(user);
        log.info("用户{}个人资料更新成功", userId);
        
        return UserInfoResponse.fromEntity(savedUser);
    }
}

