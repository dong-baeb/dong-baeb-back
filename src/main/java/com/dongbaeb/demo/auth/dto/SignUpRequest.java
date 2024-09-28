package com.dongbaeb.demo.auth.dto;

import com.dongbaeb.demo.profile.entity.Member;
import java.util.List;

public record SignUpRequest(
        String name,
        String nickname,
        String role,
        String studentNo,
        List<Long> universityId
) {
    public Member toMember(Long kakaoId) {
        return Member.builder()
                .kakaoId(kakaoId)
                .name(name)
                .nickname(nickname)
                .role(role)
                .studentNo(studentNo)
                .build();
    }
}
