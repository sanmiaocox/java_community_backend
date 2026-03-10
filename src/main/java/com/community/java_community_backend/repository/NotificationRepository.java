package com.community.java_community_backend.repository;

import com.community.java_community_backend.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /** 查询用户的通知列表（按时间倒序） */
    Page<Notification> findByReceiverIdOrderByCreatedAtDesc(Long receiverId, Pageable pageable);

    /** 查询用户的未读通知列表 */
    Page<Notification> findByReceiverIdAndIsReadFalseOrderByCreatedAtDesc(Long receiverId, Pageable pageable);

    /** 统计用户未读通知数 */
    long countByReceiverIdAndIsReadFalse(Long receiverId);

    /** 将用户所有通知标记为已读 */
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.receiverId = :receiverId AND n.isRead = false")
    int markAllAsRead(@Param("receiverId") Long receiverId);

    /** 将单条通知标记为已读 */
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id = :id AND n.receiverId = :receiverId")
    int markAsRead(@Param("id") Long id, @Param("receiverId") Long receiverId);
}

