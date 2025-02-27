package com.dongbaeb.demo.member.domain;

import com.dongbaeb.demo.global.exception.BadRequestException;
import java.util.Arrays;
import java.util.function.IntPredicate;

public enum Role {

    MEMBER("멤버", count -> count == 1),
    LEADER("리더", count -> count == 1),
    MISSIONARY("간사", count -> true),
    GRADUATE("학사", count -> count == 1);

    private final String name;
    private final IntPredicate universityCountPredicate;

    Role(String name, IntPredicate universityCountPredicate) {
        this.name = name;
        this.universityCountPredicate = universityCountPredicate;
    }

    public static Role from(String name) {
        return Arrays.stream(values())
                .filter(role -> name.equals(role.name))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("해당 " + name + "에 해당되는 역할이 존재하지 않습니다."));
    }

    public boolean isValidUniversityCount(int universityCount) {
        if (universityCount < 0) {
            throw new IllegalArgumentException("학교 수는 양수이어야 합니다.");
        }
        return universityCountPredicate.test(universityCount);
    }

    public String getName() {
        return name;
    }
}

