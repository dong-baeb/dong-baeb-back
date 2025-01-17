package com.dongbaeb.demo.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.dongbaeb.demo.global.dto.MemberAuth;
import com.dongbaeb.demo.global.exception.BadRequestException;
import com.dongbaeb.demo.global.exception.ForbiddenException;
import com.dongbaeb.demo.global.exception.ResourceNotFoundException;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.dto.MemberRequest;
import com.dongbaeb.demo.member.dto.MemberResponse;
import com.dongbaeb.demo.member.repository.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@Import({MemberService.class})
@TestPropertySource(properties = {"spring.config.location = classpath:test-application.yml"})
@ActiveProfiles("test")
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("사용자의 정보를 수정한다.")
    void updateMemberTest() {
        // given
        Member member = saveMember();
        MemberRequest memberRequest = new MemberRequest("동백이", "동백이", "멤버", "2025", List.of("광운대학교"),
                "https://xxx.xxx.xxx");
        MemberResponse expected = new MemberResponse(member.getId(), "동백이", "동백이", "멤버", "2025", List.of("광운대학교"),
                "https://xxx.xxx.xxx");

        // when
        MemberResponse actual =
                memberService.updateMember(member.getId(), memberRequest, new MemberAuth(member.getId()));

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("다른 사용자의 정보를 수정하려고 시도하면 예외가 발생한다.")
    void updateMemberWithUnAuthorizationTest() {
        // given
        Member member = saveMember();
        MemberRequest memberRequest = new MemberRequest("동백이", "동백이", "멤버", "2025", List.of("광운대학교"),
                "https://xxx.xxx.xxx");
        Long otherMemberId = member.getId() + 1L;

        // when & then
        assertThatCode(() -> memberService.updateMember(member.getId(), memberRequest, new MemberAuth(otherMemberId)))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("다른 사용자의 정보를 수정, 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 정보를 수정하려고 시도하면 예외가 발생한다.")
    void updateMemberWithNotExistsTest() {
        // given
        Long memberId = 1L; // 멤버를 저장하지 않는다.
        MemberRequest memberRequest = new MemberRequest("동백이", "동백이", "멤버", "2025", List.of("광운대학교"),
                "https://xxx.xxx.xxx");

        // when & then
        assertThatCode(() -> memberService.updateMember(memberId, memberRequest, new MemberAuth(memberId)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("해당 아이디를 가진 사용자를 찾을 수 없습니다: " + memberId);
    }

    @Test
    @DisplayName("올바르지 않은 수의 학교로 정보를 수정하려고 시도하면 예외가 발생한다.")
    void updateMemberWithInvalidUniversitiesCountTest() {
        // given
        Member member = saveMember();
        List<String> invalidUniversities = List.of(); // 멤버는 1개의 학교에 소속되어야 한다.
        MemberRequest memberRequest = new MemberRequest("동백이", "동백이", "멤버", "2025", invalidUniversities,
                "https://xxx.xxx.xxx");

        // when & then
        assertThatCode(() -> memberService.updateMember(member.getId(), memberRequest, new MemberAuth(member.getId())))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("소속될 수 있는 학교의 개수가 올바르지 않습니다.");
    }

    @Test
    @DisplayName("사용자의 정보를 삭제한다.")
    void deleteMemberTest() {
        // given
        Member member = saveMember();

        // when & then
        assertThatCode(() -> memberService.deleteMember(member.getId(), new MemberAuth(member.getId())))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("다른 사용자의 정보를 삭제하려고 시도하면 예외가 발생한다.")
    void deleteMemberWithUnAuthorizationTest() {
        // given
        Member member = saveMember();
        Long otherMemberId = member.getId() + 1L;

        // when & then
        assertThatCode(() -> memberService.deleteMember(member.getId(), new MemberAuth(otherMemberId)))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("다른 사용자의 정보를 수정, 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 정보를 삭제하려고 시도하면 예외가 발생한다.")
    void deleteMemberWithNotExistsTest() {
        // given
        Long memberId = 1L; // 멤버를 저장하지 않는다.

        // when & then
        assertThatCode(() -> memberService.deleteMember(memberId, new MemberAuth(memberId)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("해당 아이디를 가진 사용자를 찾을 수 없습니다: " + memberId);
    }

    private Member saveMember() {
        Member member = new Member(1L, "멤버", "동백", "동백", "https://xxx.xxx.xxx", "2025");
        return memberRepository.save(member);
    }
}
