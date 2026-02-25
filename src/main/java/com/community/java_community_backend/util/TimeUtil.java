package com.community.java_community_backend.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * 将LocalDateTime转换为"X分钟前"、"X小时前"等格式
     */
    public static String getTimeAgo(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        
        Duration duration = Duration.between(dateTime, LocalDateTime.now());
        long minutes = duration.toMinutes();
        
        if (minutes < 1) {
            return "刚刚";
        }
        if (minutes < 60) {
            return minutes + "分钟前";
        }
        
        long hours = duration.toHours();
        if (hours < 24) {
            return hours + "小时前";
        }
        
        long days = duration.toDays();
        if (days < 30) {
            return days + "天前";
        }
        
        return dateTime.format(DATE_FORMATTER);
    }
}

