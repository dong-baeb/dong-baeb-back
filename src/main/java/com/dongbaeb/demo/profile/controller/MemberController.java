package com.dongbaeb.demo.profile.controller;

import com.dongbaeb.demo.profile.dto.MemberRequest;
import com.dongbaeb.demo.profile.dto.MemberResponse;
import com.dongbaeb.demo.profile.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    //처음 프로필 화면 들어갈때 get으로 요청 안해줘도 되는 걸까요..? 웹에서는 get으로 화면을 이동시켜줘야했었는데 필요없다면 지우겠습니다!
    @GetMapping("/profile")
    public ResponseEntity<String> getExample() {
        return ResponseEntity.ok("Example GET response");
    }

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody MemberRequest memberRequest) {
        MemberResponse createdMember = memberService.createMember(memberRequest);
        return ResponseEntity.ok(createdMember);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @Valid @RequestBody MemberRequest memberRequest) {
        MemberResponse updatedMember = memberService.updateMember(id, memberRequest);
        return ResponseEntity.ok(updatedMember);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

}
