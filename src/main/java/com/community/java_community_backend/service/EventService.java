package com.community.java_community_backend.service;

import com.community.java_community_backend.dto.request.CreateEventRequest;
import com.community.java_community_backend.dto.request.UpdateEventRequest;
import com.community.java_community_backend.dto.response.EventParticipantResponse;
import com.community.java_community_backend.dto.response.EventResponse;
import com.community.java_community_backend.entity.Event;
import com.community.java_community_backend.entity.EventParticipant;
import com.community.java_community_backend.entity.Movie;
import com.community.java_community_backend.entity.User;
import com.community.java_community_backend.entity.Notification;
import com.community.java_community_backend.repository.EventParticipantRepository;
import com.community.java_community_backend.repository.EventRepository;
import com.community.java_community_backend.repository.MovieRepository;
import com.community.java_community_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 活动服务
 */
@Service
@RequiredArgsConstructor
public class EventService {
    
    private final EventRepository eventRepository;
    private final EventParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final NotificationService notificationService;
    
    /**
     * 创建活动
     */
    @Transactional
    public EventResponse createEvent(Long userId, CreateEventRequest request) {
        // 验证用户存在
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 验证电影存在
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("电影不存在"));
        
        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setImageUrl(request.getImageUrl());
        event.setEventDate(request.getEventDate());
        event.setRegistrationDeadline(request.getRegistrationDeadline());
        event.setEndTime(request.getEndTime());
        event.setLocation(request.getLocation());
        event.setMaxParticipants(request.getMaxParticipants());
        event.setType(request.getType());
        event.setDescription(request.getDescription());
        event.setRegistrationNotice(request.getRegistrationNotice());
        event.setMovie(movie);
        event.setMovieTmdbId(movie.getTmdbId());
        event.setCreator(creator);
        event.setParticipants(0);
        
        Event saved = eventRepository.save(event);
        return convertToResponse(saved, userId);
    }
    
    /**
     * 获取活动列表（分页）
     */
    public Page<EventResponse> getEvents(Long currentUserId, Pageable pageable) {
        Page<Event> events = eventRepository.findAllByOrderByCreatedAtDesc(pageable);
        return events.map(event -> convertToResponse(event, currentUserId));
    }
    
    /**
     * 根据类型获取活动列表
     */
    public Page<EventResponse> getEventsByType(Long currentUserId, String type, Pageable pageable) {
        Page<Event> events = eventRepository.findByTypeOrderByCreatedAtDesc(type, pageable);
        return events.map(event -> convertToResponse(event, currentUserId));
    }
    
    /**
     * 根据电影ID获取活动列表
     */
    public Page<EventResponse> getEventsByMovieId(Long currentUserId, Long movieId, Pageable pageable) {
        Page<Event> events = eventRepository.findByMovieIdOrderByCreatedAtDesc(movieId, pageable);
        return events.map(event -> convertToResponse(event, currentUserId));
    }
    
    /**
     * 搜索活动
     */
    public Page<EventResponse> searchEvents(Long currentUserId, String keyword, Pageable pageable) {
        Page<Event> events = eventRepository.searchEvents(keyword, pageable);
        return events.map(event -> convertToResponse(event, currentUserId));
    }
    
    /**
     * 获取活动详情
     */
    public EventResponse getEventById(Long eventId, Long currentUserId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("活动不存在"));
        return convertToResponse(event, currentUserId);
    }
    
    /**
     * 更新活动
     */
    @Transactional
    public EventResponse updateEvent(Long eventId, Long userId, UpdateEventRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("活动不存在"));
        
        // 验证只有创建人可以修改
        if (!event.getCreator().getId().equals(userId)) {
            throw new RuntimeException("只有创建人可以修改活动");
        }
        
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        if (request.getImageUrl() != null) {
            event.setImageUrl(request.getImageUrl());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }
        if (request.getRegistrationDeadline() != null) {
            event.setRegistrationDeadline(request.getRegistrationDeadline());
        }
        if (request.getEndTime() != null) {
            event.setEndTime(request.getEndTime());
        }
        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }
        if (request.getMaxParticipants() != null) {
            event.setMaxParticipants(request.getMaxParticipants());
        }
        if (request.getType() != null) {
            event.setType(request.getType());
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getRegistrationNotice() != null) {
            event.setRegistrationNotice(request.getRegistrationNotice());
        }
        if (request.getMovieId() != null) {
            Movie movie = movieRepository.findById(request.getMovieId())
                    .orElseThrow(() -> new RuntimeException("电影不存在"));
            event.setMovie(movie);
            event.setMovieTmdbId(movie.getTmdbId());
        }
        
        Event updated = eventRepository.save(event);
        return convertToResponse(updated, userId);
    }
    
    /**
     * 删除活动
     */
    @Transactional
    public void deleteEvent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("活动不存在"));
        
        // 验证只有创建人可以删除
        if (!event.getCreator().getId().equals(userId)) {
            throw new RuntimeException("只有创建人可以删除活动");
        }
        
        eventRepository.delete(event);
    }
    
    /**
     * 参加活动
     */
    @Transactional
    public void joinEvent(Long eventId, Long userId, String participantPhone, String participantNickname, 
                          String participantWechat, String participantQq) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("活动不存在"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 检查是否已参加
        if (participantRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new RuntimeException("已经参加过该活动");
        }
        
        // 检查报名截止时间
        if (event.getRegistrationDeadline() != null && 
            LocalDateTime.now().isAfter(event.getRegistrationDeadline())) {
            throw new RuntimeException("报名已截止");
        }
        
        // 检查是否已满员
        if (event.getParticipants() >= event.getMaxParticipants()) {
            throw new RuntimeException("活动已满员");
        }
        
        // 创建参与记录
        EventParticipant participant = new EventParticipant();
        participant.setEvent(event);
        participant.setUser(user);
        participant.setParticipantPhone(participantPhone);
        participant.setParticipantNickname(participantNickname);
        participant.setParticipantWechat(participantWechat);
        participant.setParticipantQq(participantQq);
        participantRepository.save(participant);
        
        // 更新参与人数
        event.setParticipants(event.getParticipants() + 1);
        eventRepository.save(event);

        // 通知活动创建者（非创建者报名才通知）
        notificationService.createNotification(
                event.getCreator().getId(), userId,
                Notification.NotificationType.EVENT_JOIN,
                "EVENT", eventId, null);
    }
    
    /**
     * 取消参加活动
     */
    @Transactional
    public void leaveEvent(Long eventId, Long userId) {
        EventParticipant participant = participantRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new RuntimeException("未参加该活动"));
        
        Event event = participant.getEvent();
        
        // 删除参与记录
        participantRepository.delete(participant);
        
        // 更新参与人数
        event.setParticipants(Math.max(0, event.getParticipants() - 1));
        eventRepository.save(event);

        // 通知活动创建者（非创建者退出才通知）
        notificationService.createNotification(
                event.getCreator().getId(), userId,
                Notification.NotificationType.EVENT_QUIT,
                "EVENT", eventId, null);
    }
    
    /**
     * 获取活动参与者列表
     */
    public List<EventParticipantResponse> getEventParticipants(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new RuntimeException("活动不存在");
        }
        
        List<EventParticipant> participants = participantRepository.findByEventIdOrderByJoinedAtAsc(eventId);
        return participants.stream()
                .map(this::convertToParticipantResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取用户参加的活动
     */
    public Page<EventResponse> getUserJoinedEvents(Long userId, Long currentUserId, Pageable pageable) {
        Page<EventParticipant> participants = participantRepository.findByUserIdOrderByJoinedAtDesc(userId, pageable);
        return participants.map(participant -> convertToResponse(participant.getEvent(), currentUserId));
    }
    
    /**
     * 获取用户创建的活动
     */
    public Page<EventResponse> getUserCreatedEvents(Long userId, Long currentUserId, Pageable pageable) {
        Page<Event> events = eventRepository.findByCreatorIdOrderByCreatedAtDesc(userId, pageable);
        return events.map(event -> convertToResponse(event, currentUserId));
    }
    
    /**
     * 检查用户是否已参加活动
     */
    public boolean isUserJoined(Long eventId, Long userId) {
        return participantRepository.existsByEventIdAndUserId(eventId, userId);
    }
    
    /**
     * 转换为响应DTO
     */
    private EventResponse convertToResponse(Event event, Long currentUserId) {
        boolean isParticipant = participantRepository.existsByEventIdAndUserId(event.getId(), currentUserId);
        boolean isCreator = event.getCreator().getId().equals(currentUserId);
        
        Movie movie = event.getMovie();
        User creator = event.getCreator();
        
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .imageUrl(event.getImageUrl())
                .eventDate(event.getEventDate())
                .registrationDeadline(event.getRegistrationDeadline())
                .endTime(event.getEndTime())
                .location(event.getLocation())
                .participants(event.getParticipants())
                .maxParticipants(event.getMaxParticipants())
                .type(event.getType())
                .description(event.getDescription())
                .registrationNotice(event.getRegistrationNotice())
                .movieId(movie != null ? movie.getId() : null)
                .movieTmdbId(event.getMovieTmdbId())
                .movieTitle(movie != null ? movie.getTitle() : null)
                .moviePosterUrl(movie != null ? movie.getPosterUrl() : null)
                .creatorId(creator.getId())
                .creatorUsername(creator.getUsername())
                .creatorAvatar(creator.getAvatar())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .isParticipant(isParticipant)
                .isCreator(isCreator)
                .build();
    }
    
    /**
     * 转换为参与者响应DTO
     */
    private EventParticipantResponse convertToParticipantResponse(EventParticipant participant) {
        User user = participant.getUser();
        return EventParticipantResponse.builder()
                .id(participant.getId())
                .userId(user.getId())
                .username(user.getUsername())
                .userCode(user.getUserCode())
                .avatar(user.getAvatar())
                .participantPhone(participant.getParticipantPhone())
                .participantNickname(participant.getParticipantNickname())
                .participantWechat(participant.getParticipantWechat())
                .participantQq(participant.getParticipantQq())
                .joinedAt(participant.getJoinedAt())
                .build();
    }
}

