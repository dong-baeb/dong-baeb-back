package com.dongbaeb.demo.notice.dto;

import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.notice.domain.Notice;
import com.dongbaeb.demo.notice.domain.NoticePhoto;
import com.dongbaeb.demo.notice.domain.NoticeUniversity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties.UiService;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record NoticeResponse(
        @Schema(description = "공지 ID", example = "1")
        Long id,
        @Schema(description = "카테고리", example = "동서울")
        String category,
        @Schema(description = "제목", example = "연합 큰모임 공지")
        String title,
        @Schema(description = "내용", example = "동서울 연합 큰모임을 진행합니다.")
        String content,
        @Schema(description = "작성자", example = "관리자") //
        String author,
        @Schema(description = "시작 날짜", example = "2025-12-31")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate startDate,
        @Schema(description = "종료 날짜", example = "2025-12-31")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate endDate,
        @Schema(description = "사진 URL")
        List<String> imageUrls,
        @Schema(description = "학교")
        List<String> universities,

        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static NoticeResponse from(Notice notice, List<NoticePhoto> photos, List<NoticeUniversity> universities) {
        return new NoticeResponse(
                notice.getId(),
                notice.getNoticeCategory().name(),
                notice.getTitle(),
                notice.getContent(),
                notice.getAuthor().getName(),
                notice.getStartDate(),
                notice.getEndDate(),
                extractImageUrls(photos),
                extractUniversities(universities),
                notice.getCreatedAt(),
                notice.getUpdatedAt()
        );
    }

    public static NoticeResponse fromUpcomingNotice(Notice notice, List<University> universities) {
        return NoticeResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .startDate(notice.getStartDate())
                .category(notice.getNoticeCategory().name())
                .universities(extractFromEnumUniversities(universities))
                .build();
    }

    private static List<String> extractImageUrls(List<NoticePhoto> photos) {
        return photos.stream()
                .map(NoticePhoto::getImageUrl)
                .collect(Collectors.toList());
    }

    private static List<String> extractUniversities(List<NoticeUniversity> universities) {
        return universities.stream()
                .map(noticeUniversity -> noticeUniversity.getUniversity().name())
                .collect(Collectors.toList());
    }

    private static List<String> extractFromEnumUniversities(List<University> universities) {
        return universities.stream()
                .map(university -> university.name())
                .collect(Collectors.toList());
    }
}
