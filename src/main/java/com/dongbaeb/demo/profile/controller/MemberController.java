package com.dongbaeb.demo.profile.controller;

import com.dongbaeb.demo.profile.dto.MemberRequest;
import com.dongbaeb.demo.profile.dto.MemberResponse;
import com.dongbaeb.demo.profile.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Member 프로필 수정 관련 API 요청", description = "Member의 프로필을 생성,수정,삭제 등의 역할을 한다.")
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "Member의 프로필을 생성한다.", description = "직책, 이름, 닉네임 등등의 정보를 받아 Member Entity를 생성한다.")
    @PostMapping
    public ResponseEntity<Long> createMember(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "생성 요청 Request Body",
                    content = @Content(schema = @Schema(implementation = MemberRequest.class))
            )
            @Valid @RequestBody MemberRequest memberRequest) {
        MemberResponse createdMember = memberService.createMember(memberRequest);
        URI location = URI.create(String.format("/members/%d", createdMember.id()));
        return ResponseEntity.created(location).body(createdMember.id());
    }

    @Operation(summary = "Member의 프로필을 수정한다.", description = "{id}에 해당하는 번호의 Member의 프로필을 수정한다.")
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
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Member의 프로필을 삭제한다.", description = "{id}에 해당하는 번호의 Member의 프로필을 삭제한다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(
            @Parameter(description = "삭제할 Member의 Id번호")
            @PathVariable(name = "id") Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
