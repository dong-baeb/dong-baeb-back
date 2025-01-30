package com.dongbaeb.demo.notification.service;

import com.dongbaeb.demo.notification.domain.Notification;
import com.dongbaeb.demo.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public ResponseEntity<?> getAllCouncilsNotification() {
        ArrayList<Notification> notifications = notificationRepository.findAll();
        return new ResponseEntity<>(notifications, HttpStatusCode.valueOf(200));
    }
}
