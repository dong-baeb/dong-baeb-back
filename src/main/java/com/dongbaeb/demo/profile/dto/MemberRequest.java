package com.dongbaeb.demo.profile.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MemberRequest(
        @NotNull @NotEmpty String kakaoId,
        @NotNull @NotEmpty String role,
        @NotNull @NotEmpty String name,
        @NotNull @NotEmpty String nickname,
        @NotNull @NotEmpty String profileImageUrl,
        @NotNull @NotEmpty String studentNo,
        @NotNull List<Long> universityIds
) {

}
