package com.dongbaeb.demo.profile.controller;

import com.dongbaeb.demo.profile.dto.MemberRequest;
import com.dongbaeb.demo.profile.dto.MemberResponse;
import com.dongbaeb.demo.profile.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Long> createMember(@Valid @RequestBody MemberRequest memberRequest) {
        MemberResponse createdMember = memberService.createMember(memberRequest);
        URI location = URI.create(String.format("/members/%d", createdMember.id()));
        return ResponseEntity.created(location).body(createdMember.id());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMember(@PathVariable Long id, @Valid @RequestBody MemberRequest memberRequest) {
        memberService.updateMember(id, memberRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
