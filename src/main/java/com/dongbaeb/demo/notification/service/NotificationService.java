package com.dongbaeb.demo.notification.service;

import com.dongbaeb.demo.global.exception.BadRequestException;
import com.dongbaeb.demo.notification.domain.Notification;
import com.dongbaeb.demo.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public Notification readNotification(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("해당 id를 가진 공지를 찾을 수 없습니다." + id));
    }

    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new BadRequestException("해당 id를 가진 공지를 찾을 수 없습니다." + id);
        }
        notificationRepository.deleteById(id);
    }
}
