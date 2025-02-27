package com.dongbaeb.demo.notification.dto;

import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.notification.domain.Notification;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record NotificationRequest(
        @Schema(description = "카테고리", example = "동서울")
        @NotBlank(message = "카테고리는 필수 항목입니다.")
        String category,
        @Schema(description = "제목", example = "여름 수련회 공지📣")
        @NotBlank(message = "제목은 필수 항목입니다.")
        String title,
        @Schema(description = "내용", example = "여름 수련회 공지입니다.")
        @NotBlank(message = "내용은 필수 항목입니다.")
        String content,
        @Schema(description = "시작 날짜", example = "2025-12-31")
        @NotNull(message = "시작 날짜는 필수 항목입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate startDate,
        @Schema(description = "종료 날짜", example = "2025-12-31")
        @NotNull(message = "종료 날짜는 필수 항목입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate endDate,
        @Schema(description = "사진 url")
        List<String> imageUrls,
        @Schema(description = "학교")
        List<String> universities
) {
    public Notification toNotification(Member author) {
        return new Notification(category, author, title, content, startDate, endDate);
    }
}
