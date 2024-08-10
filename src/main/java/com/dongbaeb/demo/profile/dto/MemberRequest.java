package com.dongbaeb.demo.profile.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record MemberRequest(
        @NotBlank String kakaoId,
        @NotBlank String role,
        @NotBlank String name,
        @NotBlank String nickname,
        @NotBlank String profileImageUrl,
        @NotBlank String studentNo,
        @NotBlank List<Long> universityIds
) {

}
