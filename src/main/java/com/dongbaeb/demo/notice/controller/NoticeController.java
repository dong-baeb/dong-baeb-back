package com.dongbaeb.demo.notice.controller;

import com.dongbaeb.demo.global.dto.MemberAuth;
import com.dongbaeb.demo.global.exception.dto.ExceptionResponse;
import com.dongbaeb.demo.notice.dto.NoticeResponse;
import com.dongbaeb.demo.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/notices")
@RestController
public class NoticeController {
    private final NoticeService noticeService;

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
    public ResponseEntity<?> readNotice(@PathVariable("id") Long id) {
        NoticeResponse response = noticeService.readNotice(id);
        return ResponseEntity.ok(response);
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