package com.dongbaeb.demo.profile.dto;

import com.dongbaeb.demo.profile.entity.Member;
import com.dongbaeb.demo.profile.entity.University;

import java.util.List;
import java.util.stream.Collectors;

public record MemberResponse(
        Long id,
        String kakaoId,
        String role,
        String name,
        String nickname,
        String profileImageUrl,
        String studentNo,
        List<Long> universityIds
) {
    public MemberResponse(Member member) {
        this(
                member.getId(),
                member.getKakaoId(),
                member.getRole(),
                member.getName(),
                member.getNickname(),
                member.getProfileImageUrl(),
                member.getStudentNo(),
                member.getUniversities().stream()
                        .map(University::getId)
                        .collect(Collectors.toList())
        );
    }

}

