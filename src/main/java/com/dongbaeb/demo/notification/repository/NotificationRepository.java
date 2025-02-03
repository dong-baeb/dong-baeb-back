package com.dongbaeb.demo.notification.repository;

import com.dongbaeb.demo.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
