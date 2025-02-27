package com.dongbaeb.demo.notice.domain;

import com.dongbaeb.demo.global.exception.BadRequestException;
import com.dongbaeb.demo.member.domain.Role;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.IntPredicate;

public enum NoticeCategory {
    EAST_SEOUL("동서울", count -> count == 0, Set.of(Role.MISSIONARY)),
    UNIVERSITY("학교", count -> count >= 1, Set.of(Role.MISSIONARY, Role.LEADER));

    private final String category;
    private final IntPredicate universityCountPredicate;
    private final Set<Role> allowedRoles;


    NoticeCategory(String category, IntPredicate universityCountPredicate, Set<Role> availableRoles) {
        this.category = category;
        this.universityCountPredicate = universityCountPredicate;
        this.allowedRoles = EnumSet.copyOf(availableRoles);
    }

    public static NoticeCategory form(String category) {
        return Arrays.stream(values())
                .filter(noticeCategory -> category.equals(noticeCategory.category))
                .findAny()
                .orElseThrow(() -> new BadRequestException("존재하지 않는 공지 카테고리입니다."));
    }

    public boolean isRoleAllowed(Role role) {
        return allowedRoles.contains(role);
    }

    public boolean isValidUniversityCount(int universityCount) {
        if (universityCount < 0) {
            throw new IllegalArgumentException("학교 수는 양수이어야 합니다.");
        }
        return universityCountPredicate.test(universityCount);
    }
}
