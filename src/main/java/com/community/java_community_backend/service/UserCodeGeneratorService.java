package com.community.java_community_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.community.java_community_backend.repository.UserRepository;

/**
 * 用户编码生成服务
 * 负责生成用户唯一标识码
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserCodeGeneratorService {
    
    private final UserRepository userRepository;
    
    /**
     * 生成下一个用户编码
     * 格式：0001-9999
     * 
     * @return 4位数字的用户编码
     */
    @Transactional
    public synchronized String generateNextUserCode() {
        // 获取当前最大的用户编码
        String maxUserCode = userRepository.findMaxUserCode();
        
        int nextNumber;
        if (maxUserCode == null || maxUserCode.isEmpty()) {
            // 第一个用户，从0001开始
            nextNumber = 1;
        } else {
            // 解析当前最大编码并+1
            nextNumber = Integer.parseInt(maxUserCode) + 1;
        }
        
        // 检查是否超过最大值
        if (nextNumber > 9999) {
            log.error("用户编码已达到最大值9999，无法继续生成");
            throw new RuntimeException("用户数量已达上限，无法注册新用户");
        }
        
        // 格式化为4位数字
        String userCode = String.format("%04d", nextNumber);
        log.info("生成新用户编码: {}", userCode);
        
        return userCode;
    }
    
    /**
     * 验证用户编码格式是否正确
     * 
     * @param userCode 用户编码
     * @return 是否有效
     */
    public boolean isValidUserCode(String userCode) {
        if (userCode == null || userCode.length() != 4) {
            return false;
        }
        
        try {
            int number = Integer.parseInt(userCode);
            return number >= 1 && number <= 9999;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

