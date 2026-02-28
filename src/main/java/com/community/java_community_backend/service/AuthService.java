package com.community.java_community_backend.service;

import com.community.java_community_backend.dto.request.LoginRequest;
import com.community.java_community_backend.dto.request.RegisterRequest;
import com.community.java_community_backend.dto.response.LoginResponse;
import com.community.java_community_backend.dto.response.UserInfoResponse;
import com.community.java_community_backend.entity.User;
import com.community.java_community_backend.exception.BusinessException;
import com.community.java_community_backend.repository.UserRepository;
import com.community.java_community_backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 认证服务类
 * 负责用户注册、登录等认证相关业务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserCodeGeneratorService userCodeGeneratorService;
    
    /**
     * 用户注册
     */
    @Transactional
    public UserInfoResponse register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(1001, "用户名已存在");
        }
        
        // 检查手机号是否已存在
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException(1002, "手机号已被注册");
        }
        
        // 生成用户唯一编码
        String userCode = userCodeGeneratorService.generateNextUserCode();
        
        // 创建用户
        User user = new User();
        user.setUserCode(userCode);
        user.setUsername(request.getUsername());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        // 保存用户
        User savedUser = userRepository.save(user);
        log.info("用户注册成功: userId={}, userCode={}, username={}", 
            savedUser.getId(), savedUser.getUserCode(), savedUser.getUsername());
        
        return UserInfoResponse.fromEntity(savedUser);
    }
    
    /**
     * 用户登录
     */
    public LoginResponse login(LoginRequest request) {
        // 查找用户
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new BusinessException(1003, "手机号或密码错误"));
        
        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(1003, "手机号或密码错误");
        }
        
        // 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        log.info("用户登录成功: userId={}, username={}", user.getId(), user.getUsername());
        
        // 返回登录信息
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUser(UserInfoResponse.fromEntity(user));
        
        return response;
    }
}

