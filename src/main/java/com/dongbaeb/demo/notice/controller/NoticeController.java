package com.dongbaeb.demo.notice.controller;

import com.dongbaeb.demo.global.dto.MemberAuth;
import com.dongbaeb.demo.global.exception.dto.ExceptionResponse;
import com.dongbaeb.demo.notice.dto.NoticeRequest;
import com.dongbaeb.demo.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공지 관련 API", description = "공지를 조회, 작성, 수정, 삭제한다.")
@RequiredArgsConstructor
@RequestMapping("/notices")
@RestController
public class NoticeController {
    private final NoticeService noticeService;

    @Operation(
            summary = "공지 수정",
            description = "공지를 수정한다.")

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "공지 수정 성공"
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

    @PutMapping("/notices/{id}")
    public ResponseEntity<Void> updateNotice(
            @PathVariable("id") Long noticeId,
            @RequestBody @Valid NoticeRequest request,
            MemberAuth memberAuth) {
        noticeService.updateNotice(noticeId, request, memberAuth);
        return ResponseEntity.ok().build();
    }

}
