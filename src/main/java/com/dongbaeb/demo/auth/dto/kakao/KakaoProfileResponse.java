package com.dongbaeb.demo.auth.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoProfileResponse(
        @JsonProperty("nickname")
        String nickname,
        @JsonProperty("profile_image_url")
        String profileImageUrl
) {
}
