package com.dongbaeb.demo.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
        @Schema(description = "카카오 액세스 토큰", example = "액세스 토큰")
        String accessToken,
        @Schema(description = "회원가입 여부", example = "true")
        boolean isRegistered
) {

    public static LoginResponse createRegistered(String accessToken) {
        return new LoginResponse(accessToken, true);
    }

    public static LoginResponse createNotRegistered() {
        return new LoginResponse(null, false);
    }
}
