package com.dongbaeb.demo.profile.dto;

import com.dongbaeb.demo.profile.entity.Member;
import com.dongbaeb.demo.profile.entity.University;
import lombok.Builder;

import java.util.List;

@Builder
public record MemberResponse(
        Long id,
        String role,
        String name,
        String nickname,
        String profileImageUrl,
        String studentNo,
        List<Long> universityIds
) {
    public static MemberResponse fromMember(Member member, List<University> universities) {
        return MemberResponse.builder()
                .id(member.getId())
                .role(member.getRole())
                .name(member.getName())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .studentNo(member.getStudentNo())
                .universityIds(universities.stream().map(University::getId).toList())
                .build();
    }
}
