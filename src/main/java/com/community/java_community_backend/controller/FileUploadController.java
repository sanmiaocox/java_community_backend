package com.community.java_community_backend.controller;

import com.community.java_community_backend.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/api/upload")
@Slf4j
public class FileUploadController {
    
    @Value("${server.port:7070}")
    private String serverPort;
    
    /**
     * 上传图片
     */
    @PostMapping("/image")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadImage(
            @RequestParam("file") MultipartFile file) {
        
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("文件不能为空"));
        }
        
        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("只能上传图片文件"));
        }
        
        try {
            // 获取项目根目录的绝对路径
            String projectPath = System.getProperty("user.dir");
            Path uploadDir = Paths.get(projectPath, "uploads");
            
            // 创建上传目录
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
                log.info("创建上传目录: {}", uploadDir.toAbsolutePath());
            }
            
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + extension;
            
            // 保存文件
            Path filePath = uploadDir.resolve(filename);
            file.transferTo(filePath.toFile());
            
            // 只返回文件名，不返回完整URL
            Map<String, String> result = new HashMap<>();
            result.put("filename", filename);
            
            log.info("文件上传成功: {} -> {}", filePath.toAbsolutePath(), filename);
            
            return ResponseEntity.ok(ApiResponse.success(result));
            
        } catch (IOException e) {
            log.error("文件上传失败", e);
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error("文件上传失败: " + e.getMessage()));
        }
    }
}

