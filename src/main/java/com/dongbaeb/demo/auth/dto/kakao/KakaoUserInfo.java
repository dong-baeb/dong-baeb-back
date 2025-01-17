package com.dongbaeb.demo.auth.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public record KakaoUserInfo(
        @JsonProperty("id")
        Long kakaoId,
        @JsonProperty("kakao_account")
        KakaoAccountResponse kakaoAccount
) {
}
