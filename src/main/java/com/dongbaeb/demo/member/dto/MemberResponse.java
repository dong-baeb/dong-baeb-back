package com.dongbaeb.demo.member.dto;

import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.University;
import java.util.List;
import lombok.Builder;

@Builder
public record MemberResponse(
        Long id,
        String role,
        String name,
        String nickname,
        String profileImageUrl,
        String studentNo,
        List<String> universities
) {
    public static MemberResponse fromMember(Member member, List<University> universities) {
        return MemberResponse.builder()
                .id(member.getId())
                .role(member.getRole())
                .name(member.getName())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .studentNo(member.getStudentNo())
                .universities(universities.stream().map(University::getName).toList())
                .build();
    }
}
