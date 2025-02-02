package com.dongbaeb.demo.notification.controller;

import com.dongbaeb.demo.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/notification")
@RestController
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/{id}")
    public ResponseEntity<?> readNotification(@PathVariable("id") Long id) {
        return ResponseEntity.ok(notificationService.readNotification(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable("id") Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent()
                .build();
    }
}
