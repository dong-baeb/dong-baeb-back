package com.dongbaeb.demo.profile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record MemberRequest(
        // 이후 다른 소셜로그인 아이디가 추가될 수도 있어 kakaoId 대신 socialId와 socialProvider 같은 걸로 바꾸어야할 것 같습니다..
        @NotBlank(message = "카카오 아이디는 필수 항목입니다.")
        String kakaoId,

        @NotBlank(message = "역할은 필수 항목입니다.")
        String role,

        @NotBlank(message = "이름은 필수 항목입니다.")
        String name,

        @NotBlank(message = "닉네임은 필수 항목입니다.")
        String nickname,

        @NotBlank(message = "프로필 이미지는 필수 항목입니다.")
        String profileImageUrl,

        @NotBlank(message = "입학년도는 필수 항목입니다.")
        String studentNo,

        @NotNull(message = "대학 ID 리스트는 필수 항목입니다.")
        @Size(min = 1, message = "최소 하나의 대학 ID가 필요합니다.")
        List<Long> universityIds
) {

}
