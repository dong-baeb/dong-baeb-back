package com.dongbaeb.demo.auth.dto;

import com.dongbaeb.demo.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record SignUpResponse(
        @Schema(description = "멤버 id", example = "1")
        Long id,
        @Schema(description = "동뱁 액세스 토큰", example = "액세스 토큰")
        String accessToken
) {

    public static SignUpResponse from(Member member, String accessToken) {
        return new SignUpResponse(member.getId(), accessToken);
    }
}
