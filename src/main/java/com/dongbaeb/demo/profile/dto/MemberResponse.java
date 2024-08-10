package com.dongbaeb.demo.profile.dto;

import java.util.List;

public record MemberResponse(
        Long id,
        String role,
        String name,
        String nickname,
        String profileImageUrl,
        String studentNo,
        List<Long> universityIds
) {

}

