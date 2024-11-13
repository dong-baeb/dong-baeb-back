package com.dongbaeb.demo.auth.dto;

import com.dongbaeb.demo.member.domain.Member;
import java.util.List;

public record SignUpRequest(
        String name,
        String nickname,
        String role,
        String studentNo,
        List<String> universities
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
