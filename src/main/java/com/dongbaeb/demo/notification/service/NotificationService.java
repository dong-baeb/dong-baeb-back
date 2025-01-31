package com.dongbaeb.demo.notification.service;

import com.dongbaeb.demo.notification.domain.Notification;
import com.dongbaeb.demo.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public ResponseEntity<?> getAllCouncilsNotification(int page) {
        int pageSize = 15;
        int offset = (page - 1) * pageSize;
        ArrayList<Notification> notifications = notificationRepository.findPagedWholeEntities(pageSize,offset);
        return new ResponseEntity<>(notifications, HttpStatusCode.valueOf(200));
    }
}
