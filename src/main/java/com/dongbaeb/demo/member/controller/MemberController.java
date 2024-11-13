package com.dongbaeb.demo.member.controller;

import com.dongbaeb.demo.member.dto.MemberRequest;
import com.dongbaeb.demo.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMember(
            @Parameter(description = "수정할 Member의 Id 번호")
            @PathVariable(name = "id") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "수정 요청 Request Body",
                    content = @Content(schema = @Schema(implementation = MemberRequest.class))
            )
            @Valid @RequestBody MemberRequest memberRequest) {
        memberService.updateMember(id, memberRequest);
        return ResponseEntity.noContent()
                .build();
    }

    @Operation(summary = "Member의 프로필을 삭제한다.", description = "{id}에 해당하는 번호의 Member의 프로필을 삭제한다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(
            @Parameter(description = "삭제할 Member의 Id번호")
            @PathVariable(name = "id") Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent()
                .build();
    }
}


