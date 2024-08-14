package com.dongbaeb.demo.auth.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInfoResponse(
    @JsonProperty("id") Long kakaoId,
    @JsonProperty("kakao_account") KakaoAccountResponse kakaoAccount
) {
}
