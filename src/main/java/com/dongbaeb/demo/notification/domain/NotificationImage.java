package com.dongbaeb.demo.notification.domain;

import jakarta.persistence.*;
import lombok.Getter;


@Getter
@Entity
public class NotificationImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;
}
