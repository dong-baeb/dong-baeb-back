package com.dongbaeb.demo.notification.domain;

import com.dongbaeb.demo.global.exception.BadRequestException;
import java.util.Arrays;

public enum NotificationCategory {
    EAST_SEOUL("동서울"),
    UNIVERSITY("학교");

    private final String category;

    NotificationCategory(String category) {
        this.category = category;
    }

    public static NotificationCategory form(String category) {
        return Arrays.stream(values())
                .filter(notificationCategory -> category.equals(notificationCategory.category))
                .findAny()
                .orElseThrow(() -> new BadRequestException("존재하지 않는 공지 카테고리입니다."));
    }
}
