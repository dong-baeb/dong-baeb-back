package com.dongbaeb.demo.notification.controller;

import com.dongbaeb.demo.global.dto.MemberAuth;
import com.dongbaeb.demo.global.exception.dto.ExceptionResponse;
import com.dongbaeb.demo.notification.dto.NotificationRequest;
import com.dongbaeb.demo.notification.service.NotificationService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공지 관련 API", description = "공지를 조회, 작성, 수정, 삭제한다.")
@RequiredArgsConstructor
@RequestMapping("/notifications")
@RestController
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(
            summary = "공지 작성",
            description = "공지를 작성한다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "공지 작성 성공"
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
    public ResponseEntity<Void> createNotification(
            @Valid @RequestBody NotificationRequest request,
            MemberAuth memberAuth) {
        Long notificationId = notificationService.createNotification(request, memberAuth);
        return ResponseEntity.created(URI.create("/notifications/" + notificationId))
                .build();
    }
}
