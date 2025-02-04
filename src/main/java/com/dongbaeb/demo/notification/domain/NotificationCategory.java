package com.dongbaeb.demo.notification.domain;

import com.dongbaeb.demo.global.exception.BadRequestException;
import com.dongbaeb.demo.member.domain.Role;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public enum NotificationCategory {
    EAST_SEOUL("동서울", Set.of(Role.MISSIONARY)),
    UNIVERSITY("학교", Set.of(Role.MISSIONARY, Role.LEADER));

    private final String category;
    private final Set<Role> allowedRoles;

    NotificationCategory(String category, Set<Role> availableRoles) {
        this.category = category;
        this.allowedRoles = EnumSet.copyOf(availableRoles);
    }

    public static NotificationCategory form(String category) {
        return Arrays.stream(values())
                .filter(notificationCategory -> category.equals(notificationCategory.category))
                .findAny()
                .orElseThrow(() -> new BadRequestException("존재하지 않는 공지 카테고리입니다."));
    }

    public boolean isRoleAllowed(Role role) {
        return allowedRoles.contains(role);
    }
}
