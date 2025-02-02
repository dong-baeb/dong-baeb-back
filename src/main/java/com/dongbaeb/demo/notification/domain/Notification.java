package com.dongbaeb.demo.notification.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String writer;

//    private Date date;

    @Column(nullable = false)
    private String message;

    @OneToMany(mappedBy = "notification")
    private List<NotificationImage> images;
}
