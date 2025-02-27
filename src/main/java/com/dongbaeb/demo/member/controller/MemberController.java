package com.dongbaeb.demo.member.controller;

import com.dongbaeb.demo.global.dto.MemberAuth;
import com.dongbaeb.demo.global.exception.dto.ExceptionResponse;
import com.dongbaeb.demo.member.dto.MemberRequest;
import com.dongbaeb.demo.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member 프로필 수정 관련 API 요청", description = "Member의 프로필을 생성,수정,삭제 등의 역할을 한다.")
@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {
    private final MemberService memberService;

    @Operation(
            summary = "멤버 정보 수정",
            description = "멤버의 정보를 수정한다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "멤버 정보 수정 성공"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않은 액세스 토큰으로 인한 멤버 정보 수정 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "다른 사용자의 정보 수정으로 인한 수정 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMember(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody MemberRequest memberRequest,
            MemberAuth memberAuth
    ) {
        memberService.updateMember(id, memberRequest, memberAuth);
        return ResponseEntity.noContent()
                .build();
    }

    @Operation(
            summary = "멤버 정보 삭제",
            description = "멤버의 정보를 삭제한다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "멤버 정보 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않은 액세스 토큰으로 인한 멤버 정보 삭제 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "다른 사용자의 정보 삭제로 인한 삭제 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable(name = "id") Long id, MemberAuth memberAuth) {
        memberService.deleteMember(id, memberAuth);
        return ResponseEntity.noContent()
                .build();
    }
}


