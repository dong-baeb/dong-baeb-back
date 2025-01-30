package com.dongbaeb.demo.notification.controller;

import com.dongbaeb.demo.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/notification")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/councils")
    public ResponseEntity<?> getAllCouncilsNotification() {
        ResponseEntity<?> response = notificationService.getAllCouncilsNotification();
        return response;
    }
}
