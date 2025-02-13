package com.dongbaeb.demo.notification.repository;

import com.dongbaeb.demo.notification.domain.Notification;
import com.dongbaeb.demo.notification.domain.NotificationPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface NotificationPhotoRepository extends JpaRepository<NotificationPhoto,Long> {

    ArrayList<NotificationPhoto> findByNotification(Notification notification);
}
