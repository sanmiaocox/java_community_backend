package com.community.java_community_backend.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 关闭跨域防护（方便本地测试，生产环境按需开启）
                .csrf(csrf -> csrf.disable())
                // 配置请求授权规则
                .authorizeHttpRequests(auth -> auth
                        // 核心：放行测试接口
                        .requestMatchers("/hello").permitAll()
                        .requestMatchers("/api/tmdb/**").permitAll()
                        // 其他接口需要认证（登录后才能访问，按需调整）
                        .anyRequest().authenticated()
                )
                // 禁用表单登录，避免自动跳转到登录页面
                .formLogin(form -> form.disable())
                // 使用 HTTP Basic 认证（可选，或者完全禁用）
                .httpBasic(basic -> basic.disable());
        return http.build();
    }
}
