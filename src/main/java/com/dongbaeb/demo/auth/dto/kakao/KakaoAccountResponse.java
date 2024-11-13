package com.dongbaeb.demo.auth.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoAccountResponse(
        @JsonProperty("profile")
        KakaoProfileResponse kakaoProfile
) {
}
