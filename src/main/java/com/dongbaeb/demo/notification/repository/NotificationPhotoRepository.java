package com.dongbaeb.demo.notification.repository;

import com.dongbaeb.demo.notification.domain.NotificationPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationPhotoRepository extends JpaRepository<NotificationPhoto, Long> {
}
