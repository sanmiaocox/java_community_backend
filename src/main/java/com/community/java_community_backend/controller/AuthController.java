package com.community.java_community_backend.controller;

import com.community.java_community_backend.dto.request.LoginRequest;
import com.community.java_community_backend.dto.request.RegisterRequest;
import com.community.java_community_backend.dto.response.ApiResponse;
import com.community.java_community_backend.dto.response.LoginResponse;
import com.community.java_community_backend.dto.response.UserInfoResponse;
import com.community.java_community_backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 处理用户注册、登录等认证相关请求
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor


public class AuthController {

    private final AuthService authService;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ApiResponse<UserInfoResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserInfoResponse response = authService.register(request);
        return ApiResponse.success("注册成功", response);
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ApiResponse.success("登录成功", response);
    }
}

