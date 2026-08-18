package com.mediconnect.service.impl;

import com.mediconnect.dto.NotificationResponseDto;
import com.mediconnect.entity.Notification;
import com.mediconnect.entity.User;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.NotificationRepository;
import com.mediconnect.repository.UserRepository;
import com.mediconnect.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void notify(Long userId, String type, String title, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Notification.NotificationType notificationType;
        try {
            notificationType = Notification.NotificationType.valueOf(type);
        } catch (IllegalArgumentException e) {
            notificationType = Notification.NotificationType.GENERAL;
        }

        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .type(notificationType)
                .read(false)
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationResponseDto> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(NotificationResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponseDto> getUnreadByUserId(Long userId) {
        return notificationRepository.findByUserIdAndReadFalse(userId).stream()
                .map(NotificationResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }

    @Override
    @Transactional
    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", id));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
