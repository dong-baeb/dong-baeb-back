package com.dongbaeb.demo.auth.dto;

import com.dongbaeb.demo.member.domain.Member;

public record SignUpResponse(
        Long id,
        String accessToken
) {

    public static SignUpResponse from(Member member, String accessToken) {
        return new SignUpResponse(member.getId(), accessToken);
    }
}
