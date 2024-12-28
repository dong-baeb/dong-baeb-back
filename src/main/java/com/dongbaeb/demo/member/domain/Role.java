package com.dongbaeb.demo.member.domain;

import com.dongbaeb.demo.global.exception.BadRequestException;
import java.util.Arrays;
import lombok.Getter;

// TODO: Role에 따른 학교 수 검증 필요
@Getter
public enum Role {

    MEMBER("멤버"),
    LEADER("리더"),
    MISSIONARY("간사"),
    BACHELOR("학사");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public static Role from(String name) {
        return Arrays.stream(values())
                .filter(role -> name.equals(role.name))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("해당 " + name + "에 해당되는 역할이 존재하지 않습니다."));
    }
}

