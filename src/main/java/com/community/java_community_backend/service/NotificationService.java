package com.community.java_community_backend.service;

import com.community.java_community_backend.dto.response.NotificationResponse;
import com.community.java_community_backend.entity.Notification;
import com.community.java_community_backend.entity.User;
import com.community.java_community_backend.repository.NotificationRepository;
import com.community.java_community_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统通知服务
 * 负责通知的创建、查询、标记已读
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    // ===================== 通知触发（内部调用） =====================

    /**
     * 创建通知（通用入口，供其他 Service 调用）
     * 如果 senderId == receiverId 则不创建（自己操作自己的内容不通知）
     *
     * @param receiverId 接收者ID
     * @param senderId   触发者ID（系统通知传 null）
     * @param type       通知类型
     * @param targetType 关联对象类型（FEED / COMMENT / USER / EVENT / null）
     * @param targetId   关联对象ID（null 时不填）
     * @param content    通知摘要文本
     */
    public void createNotification(Long receiverId, Long senderId,
                                   Notification.NotificationType type,
                                   String targetType, Long targetId,
                                   String content) {
        // 自己操作自己不产生通知
        if (senderId != null && senderId.equals(receiverId)) {
            return;
        }

        Notification notification = Notification.builder()
                .receiverId(receiverId)
                .senderId(senderId)
                .type(type)
                .targetType(targetType)
                .targetId(targetId)
                .content(content)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
        log.debug("创建通知: receiver={}, type={}", receiverId, type);
    }

    // ===================== 查询 =====================

    /**
     * 查询用户所有通知（分页，含已读未读）
     */
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getNotifications(Long userId, Pageable pageable) {
        return notificationRepository
                .findByReceiverIdOrderByCreatedAtDesc(userId, pageable)
                .map(this::toResponse);
    }

    /**
     * 查询用户未读通知（分页）
     */
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUnreadNotifications(Long userId, Pageable pageable) {
        return notificationRepository
                .findByReceiverIdAndIsReadFalseOrderByCreatedAtDesc(userId, pageable)
                .map(this::toResponse);
    }

    /**
     * 获取用户未读通知数量
     */
    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByReceiverIdAndIsReadFalse(userId);
    }

    // ===================== 标记已读 =====================

    /**
     * 将单条通知标记为已读
     */
    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        int updated = notificationRepository.markAsRead(notificationId, userId);
        if (updated == 0) {
            throw new RuntimeException("通知不存在或无权限");
        }
    }

    /**
     * 将当前用户所有通知标记为已读
     */
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
    }

    // ===================== 转换 =====================

    private NotificationResponse toResponse(Notification n) {
        NotificationResponse.SenderDTO senderDTO = null;
        if (n.getSenderId() != null) {
            // 懒加载已初始化则直接取，否则查库
            User sender = n.getSender() != null ? n.getSender()
                    : userRepository.findById(n.getSenderId()).orElse(null);
            if (sender != null) {
                senderDTO = NotificationResponse.SenderDTO.builder()
                        .id(sender.getId())
                        .username(sender.getUsername())
                        .avatar(sender.getAvatar())
                        .build();
            }
        }

        return NotificationResponse.builder()
                .id(n.getId())
                .type(n.getType())
                .sender(senderDTO)
                .targetType(n.getTargetType())
                .targetId(n.getTargetId())
                .content(n.getContent())
                .isRead(n.getIsRead())
                .createdAt(n.getCreatedAt())
                .build();
    }
}

