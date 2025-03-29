package com.dongbaeb.demo.notice.dto;

import com.dongbaeb.demo.notice.domain.Notice;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record NoticeResponse(
        Long id,
        String category,
        String title,
        String content,
        String authorName,
        LocalDate startDate,
        LocalDate endDate,
        List<String> imageUrls,
        List<String> universities,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static NoticeResponse from(Notice notice) {
        return null;
    }
}
