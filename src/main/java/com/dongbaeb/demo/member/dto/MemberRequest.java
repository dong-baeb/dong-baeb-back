package com.dongbaeb.demo.member.dto;

import com.dongbaeb.demo.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Builder;

@Builder
public record MemberRequest(
        @NotBlank(message = "직책은 필수 항목입니다.")
        String role,
        @NotBlank(message = "이름은 필수 항목입니다.")
        String name,
        @NotBlank(message = "닉네임은 필수 항목입니다.")
        String nickname,
        @NotBlank(message = "프로필 이미지는 필수 항목입니다.")
        String profileImageUrl,
        String studentNo,
        List<String> universities
) {
    public Member toMember() {
        return Member.builder()
                .role(this.role)
                .name(this.name)
                .nickname(this.nickname)
                .studentNo(this.studentNo)
                .build();
    }
}
