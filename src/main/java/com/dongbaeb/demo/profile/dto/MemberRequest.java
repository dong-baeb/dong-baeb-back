package com.dongbaeb.demo.profile.dto;

import com.dongbaeb.demo.profile.entity.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record MemberRequest(
        @NotBlank(message = "카카오 아이디는 필수 항목입니다.")
        String kakaoId,

        @NotBlank(message = "직책은 필수 항목입니다.")
        String role,

        @NotBlank(message = "이름은 필수 항목입니다.")
        String name,

        @NotBlank(message = "닉네임은 필수 항목입니다.")
        String nickname,

        @NotBlank(message = "프로필 이미지는 필수 항목입니다.")
        String profileImageUrl,

        String studentNo,

        List<Long> universityIds
) {
        public Member toMember() {
                return Member.builder()
                        .kakaoId(this.kakaoId)
                        .role(this.role)
                        .name(this.name)
                        .nickname(this.nickname)
                        .profileImageUrl(this.profileImageUrl)
                        .studentNo(this.studentNo)
                        .build();
        }
}
