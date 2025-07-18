package com.dongbaeb.demo.notice.controller;

import com.dongbaeb.demo.global.dto.MemberAuth;
import com.dongbaeb.demo.global.exception.dto.ExceptionResponse;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.notice.domain.NoticeCategory;
import com.dongbaeb.demo.notice.dto.NoticeRequest;
import com.dongbaeb.demo.notice.dto.NoticeResponse;
import com.dongbaeb.demo.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공지 관련 API", description = "공지를 조회, 작성, 수정, 삭제한다.")
@RequiredArgsConstructor
@RequestMapping("/notices")
@RestController
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "조건에 맞는 공지를 가져온다.", description = "category와 name에 맞는 공지를 가져온다. page, size를 통해 가져오는 양을 조절할 수 있다.")
    @GetMapping("/")
    public ResponseEntity<List<NoticeResponse>> getNotices(
            @Parameter(description = "공지 카테고리 정보")
            @RequestParam("category") NoticeCategory noticeCategory,
            @Parameter(description = "조회 하고 싶은 학교 이름")
            @RequestParam("name") University name,
            Pageable pageable,
            MemberAuth memberAuth) {

        List<NoticeResponse> notices;

        if (NoticeCategory.EAST_SEOUL.equals(noticeCategory)) {
            notices = noticeService.getAllCouncilsNotification(pageable);
        } else {
            notices = noticeService.getByUniversityName(name, pageable, memberAuth);
        }

        return ResponseEntity.ok(notices);
    }


    @Operation(
            summary = "공지 작성",
            description = "공지를 작성한다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "공지 작성 성공"
            )
    })
    @PostMapping
    public ResponseEntity<Void> createNotice(
            @Valid @RequestBody NoticeRequest request,
            MemberAuth memberAuth) {
        Long noticeId = noticeService.createNotice(request, memberAuth);
        return ResponseEntity.created(URI.create("/notices/" + noticeId))
                .build();

    }

    @Operation(
            summary = "공지 조회",
            description = "공지 ID로 공지를 조회한다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "공지 조회 성공",
                    content = @Content(schema = @Schema(implementation = NoticeResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "공지 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponse> readNotice(
            @PathVariable("id") Long id,
            MemberAuth memberAuth) {
        NoticeResponse response = noticeService.readNotice(id, memberAuth);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "다가오는 공지 조회",
            description = "다가오는 공지를 조회한다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "다가오는 공지 조회 성공",
                    content = @Content(schema = @Schema(implementation = NoticeResponse.class))
            ),
    })
    @GetMapping("/upcoming")
    public ResponseEntity<List<NoticeResponse>> readUpcomingNotice(MemberAuth memberAuth) {
        List<NoticeResponse> response = noticeService.readUpcomingNotice(memberAuth);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "멤버 ID로 공지 가져오기",
            description = "멤버 ID로 해당 멤버가 작성한 공지를 가지고 온다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "공지 조회 성공",
                    content = @Content(schema = @Schema(implementation = NoticeResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않은 액세스 토큰으로 인한 멤버 정보 조회 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "다른 사용자의 공지를 접근했습니다.",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @GetMapping("/member")
    public ResponseEntity<List<NoticeResponse>> getNoticeByMemberId(MemberAuth memberAuth) {
        List<NoticeResponse> foundNotices = noticeService.getNoticeByMemberId(memberAuth);
        return ResponseEntity.ok(foundNotices);
    }

    @Operation(
            summary = "공지 삭제",
            description = "공지 ID로 공지를 삭제한다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "공지 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않은 액세스 토큰으로 인한 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 부족으로 인한 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "공지 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(
            @PathVariable("id") Long id,
            MemberAuth memberAuth) {
        noticeService.deleteNotice(id, memberAuth);
        return ResponseEntity.noContent()
                .build();
    }
}
