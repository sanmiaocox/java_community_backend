package com.community.java_community_backend.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security配置类
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 关闭CSRF（使用JWT不需要CSRF保护）
                .csrf(csrf -> csrf.disable())
                
                // 配置Session管理为无状态（使用JWT）
                .sessionManagement(session -> 
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                
                // 配置请求授权规则
                .authorizeHttpRequests(auth -> auth
                        // 放行测试接口
                        .requestMatchers("/hello").permitAll()
                        // 放行认证接口（注册、登录）
                        .requestMatchers("/api/auth/**").permitAll()
                        // 放行上传接口
                        .requestMatchers("/api/upload/**").permitAll()
                        // 放行静态资源
                        .requestMatchers("/uploads/**").permitAll()
                        // 其他所有接口都需要认证（需要Token）
                        .anyRequest().permitAll()  // 暂时全部放行，后续实现JWT过滤器后改为authenticated()
                )
                
                // 禁用表单登录
                .formLogin(form -> form.disable())
                
                // 禁用HTTP Basic认证
                .httpBasic(basic -> basic.disable());
                
        return http.build();
    }
}
