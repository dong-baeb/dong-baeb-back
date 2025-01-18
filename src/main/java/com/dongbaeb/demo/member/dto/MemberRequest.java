package com.dongbaeb.demo.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Builder;

@Builder
public record MemberRequest(
        @Schema(description = "이름", example = "동백")
        @NotBlank(message = "이름은 필수 항목입니다.")
        String name,
        @Schema(description = "닉네임", example = "동백")
        @NotBlank(message = "닉네임은 필수 항목입니다.")
        String nickname,
        @Schema(description = "직책", example = "멤버")
        @NotBlank(message = "직책은 필수 항목입니다.")
        String role,
        @Schema(description = "학번", example = "2025")
        String studentNo,
        @Schema(description = "학교")
        @NotEmpty(message = "학교는 필수 항목입니다.")
        List<String> universities,
        @Schema(description = "프로필 이미지", example = "https://xxx.xxx.xxx")
        String profileImageUrl
) {
}
