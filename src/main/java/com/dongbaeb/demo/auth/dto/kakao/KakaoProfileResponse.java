package com.dongbaeb.demo.auth.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public record KakaoProfileResponse(
        @JsonProperty("nickname")
        String nickname,
        @JsonProperty("profile_image_url")
        String profileImageUrl
) {
}
