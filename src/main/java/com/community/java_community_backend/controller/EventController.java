package com.community.java_community_backend.controller;

import com.community.java_community_backend.dto.request.CreateEventRequest;
import com.community.java_community_backend.dto.request.UpdateEventRequest;
import com.community.java_community_backend.dto.response.ApiResponse;
import com.community.java_community_backend.dto.response.EventParticipantResponse;
import com.community.java_community_backend.dto.response.EventResponse;
import com.community.java_community_backend.service.EventService;
import com.community.java_community_backend.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 活动控制器
 */
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    
    private final EventService eventService;
    private final JwtUtil jwtUtil;
    
    /**
     * 创建活动
     */
    @PostMapping
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateEventRequest request) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        EventResponse response = eventService.createEvent(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * 获取活动列表（分页）
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<EventResponse>>> getEvents(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) String keyword) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        Pageable pageable = PageRequest.of(page, size);
        
        Page<EventResponse> events;
        if (keyword != null && !keyword.trim().isEmpty()) {
            events = eventService.searchEvents(userId, keyword, pageable);
        } else if (movieId != null) {
            events = eventService.getEventsByMovieId(userId, movieId, pageable);
        } else if (type != null && !type.trim().isEmpty()) {
            events = eventService.getEventsByType(userId, type, pageable);
        } else {
            events = eventService.getEvents(userId, pageable);
        }
        
        return ResponseEntity.ok(ApiResponse.success(events));
    }
    
    /**
     * 获取活动详情
     */
    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventResponse>> getEventById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long eventId) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        EventResponse event = eventService.getEventById(eventId, userId);
        return ResponseEntity.ok(ApiResponse.success(event));
    }
    
    /**
     * 更新活动
     */
    @PutMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventResponse>> updateEvent(
            @RequestHeader("Authorization") String token,
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventRequest request) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        EventResponse response = eventService.updateEvent(eventId, userId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * 删除活动
     */
    @DeleteMapping("/{eventId}")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(
            @RequestHeader("Authorization") String token,
            @PathVariable Long eventId) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        eventService.deleteEvent(eventId, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
    
    /**
     * 参加活动
     */
    @PostMapping("/{eventId}/join")
    public ResponseEntity<ApiResponse<Map<String, Object>>> joinEvent(
            @RequestHeader("Authorization") String token,
            @PathVariable Long eventId) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        eventService.joinEvent(eventId, userId);
        
        EventResponse event = eventService.getEventById(eventId, userId);
        Map<String, Object> result = new HashMap<>();
        result.put("joined", true);
        result.put("participants", event.getParticipants());
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    
    /**
     * 取消参加活动
     */
    @DeleteMapping("/{eventId}/join")
    public ResponseEntity<ApiResponse<Map<String, Object>>> leaveEvent(
            @RequestHeader("Authorization") String token,
            @PathVariable Long eventId) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        eventService.leaveEvent(eventId, userId);
        
        EventResponse event = eventService.getEventById(eventId, userId);
        Map<String, Object> result = new HashMap<>();
        result.put("joined", false);
        result.put("participants", event.getParticipants());
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    
    /**
     * 获取活动参与者列表
     */
    @GetMapping("/{eventId}/participants")
    public ResponseEntity<ApiResponse<List<EventParticipantResponse>>> getEventParticipants(
            @RequestHeader("Authorization") String token,
            @PathVariable Long eventId) {
        List<EventParticipantResponse> participants = eventService.getEventParticipants(eventId);
        return ResponseEntity.ok(ApiResponse.success(participants));
    }
    
    /**
     * 检查是否已参加活动
     */
    @GetMapping("/{eventId}/joined")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkJoined(
            @RequestHeader("Authorization") String token,
            @PathVariable Long eventId) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        boolean isJoined = eventService.isUserJoined(eventId, userId);
        
        Map<String, Boolean> result = new HashMap<>();
        result.put("isJoined", isJoined);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    
    /**
     * 获取用户参加的活动
     */
    @GetMapping("/user/{userId}/joined")
    public ResponseEntity<ApiResponse<Page<EventResponse>>> getUserJoinedEvents(
            @RequestHeader("Authorization") String token,
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Long currentUserId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        Pageable pageable = PageRequest.of(page, size);
        Page<EventResponse> events = eventService.getUserJoinedEvents(userId, currentUserId, pageable);
        return ResponseEntity.ok(ApiResponse.success(events));
    }
    
    /**
     * 获取用户创建的活动
     */
    @GetMapping("/user/{userId}/created")
    public ResponseEntity<ApiResponse<Page<EventResponse>>> getUserCreatedEvents(
            @RequestHeader("Authorization") String token,
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Long currentUserId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        Pageable pageable = PageRequest.of(page, size);
        Page<EventResponse> events = eventService.getUserCreatedEvents(userId, currentUserId, pageable);
        return ResponseEntity.ok(ApiResponse.success(events));
    }
}

