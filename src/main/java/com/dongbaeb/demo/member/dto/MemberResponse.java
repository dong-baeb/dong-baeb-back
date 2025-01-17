package com.dongbaeb.demo.member.dto;

import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.University;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
public record MemberResponse(
        @Schema(description = "id", example = "1")
        Long id,
        @Schema(description = "이름", example = "동백")
        String name,
        @Schema(description = "닉네임", example = "동백")
        String nickname,
        @Schema(description = "직책", example = "멤버")
        String role,
        @Schema(description = "학번", example = "2025")
        String studentNo,
        @Schema(description = "학교")
        List<String> universities,
        @Schema(description = "프로필 이미지", example = "https://xxx.xxx.xxx")
        String profileImageUrl
) {
    public static MemberResponse fromMember(Member member, List<University> universities) {
        return MemberResponse.builder()
                .id(member.getId())
                .role(member.getRole().getName())
                .name(member.getName())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .studentNo(member.getStudentNo())
                .universities(universities.stream().map(University::getName).toList())
                .build();
    }
}
