package com.dongbaeb.demo.member.domain;

import com.dongbaeb.demo.global.exception.BadRequestException;
import java.util.Arrays;
import java.util.function.IntPredicate;

public enum Role {

    MEMBER("멤버", count -> count == 1),
    LEADER("리더", count -> count == 1),
    MISSIONARY("간사", count -> true),
    BACHELOR("학사", count -> count == 1);

    private final String name;
    private final IntPredicate universitiesCountPredicate;

    Role(String name, IntPredicate universitiesCountPredicate) {
        this.name = name;
        this.universitiesCountPredicate = universitiesCountPredicate;
    }

    public static Role from(String name) {
        return Arrays.stream(values())
                .filter(role -> name.equals(role.name))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("해당 " + name + "에 해당되는 역할이 존재하지 않습니다."));
    }

    public boolean isValidUniversitiesCount(int universitiesCount) {
        if (universitiesCount < 0) {
            throw new IllegalArgumentException("학교 수는 양수이어야 합니다.");
        }
        return universitiesCountPredicate.test(universitiesCount);
    }

    public String getName() {
        return name;
    }
}

