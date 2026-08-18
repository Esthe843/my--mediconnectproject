package com.mediconnect.service;

import com.mediconnect.dto.NotificationResponseDto;

import java.util.List;

public interface NotificationService {

    void notify(Long userId, String type, String title, String message);

    List<NotificationResponseDto> getNotificationsByUserId(Long userId);

    List<NotificationResponseDto> getUnreadByUserId(Long userId);

    long getUnreadCount(Long userId);

    void markAsRead(Long id);
}
