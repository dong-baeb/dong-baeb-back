package com.dongbaeb.demo.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dongbaeb.demo.auth.dto.LoginResponse;
import com.dongbaeb.demo.auth.dto.SignUpRequest;
import com.dongbaeb.demo.auth.dto.SignUpResponse;
import com.dongbaeb.demo.auth.dto.kakao.KakaoAccountResponse;
import com.dongbaeb.demo.auth.dto.kakao.KakaoProfileResponse;
import com.dongbaeb.demo.auth.dto.kakao.KakaoUserInfo;
import com.dongbaeb.demo.auth.infrastructure.JwtTokenProvider;
import com.dongbaeb.demo.global.exception.BadRequestException;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.repository.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

// TODO: 테스트 픽스처 도입하기
// TODO: 데이터 초기화 처리(트랜잭션 사용 안하도록)
@DataJpaTest
@Import({LoginService.class, JwtTokenProvider.class})
@TestPropertySource(properties = {"spring.config.location = classpath:test-application.yml"})
@ActiveProfiles("test")
class LoginServiceTest {
    @Autowired
    LoginService loginService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 되어 있지 않은 사용자가 카카오 로그인을 수행한다.")
    void kakaoLoginWithNotRegisteredTest() {
        // given
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(1L,
                new KakaoAccountResponse(new KakaoProfileResponse("동백이", "카카오 이미지 url")));
        LoginResponse expected = new LoginResponse(null, false);

        // when & then
        assertThat(loginService.kakaoLogin(kakaoUserInfo)).isEqualTo(expected);
    }

    @Test
    @DisplayName("회원가입 되어 있는 사용자가 카카오 로그인을 수행한다.")
    void kakaoLoginWithRegisteredTest() {
        // given
        Long kakaoId = 1L;
        saveMember(kakaoId);
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(kakaoId,
                new KakaoAccountResponse(new KakaoProfileResponse("동백이", "카카오 이미지 url")));

        // when
        LoginResponse loginResponse = loginService.kakaoLogin(kakaoUserInfo);

        // then
        assertAll(
                () -> assertThat(loginResponse.isRegistered()).isTrue(),
                () -> assertThat(loginResponse.accessToken()).isNotBlank()
        );
    }

    @Test
    @DisplayName("회원가입에 성공한다.")
    void signUpSuccessTest() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest("동백", "동백", "멤버", "2025", List.of("광운대학교"),
                "https://xxx.xxx.xxx");
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(1L,
                new KakaoAccountResponse(new KakaoProfileResponse("동백이", "카카오 이미지 url")));

        // when
        SignUpResponse signUpResponse = loginService.signUp(signUpRequest, kakaoUserInfo);

        // then
        assertAll(
                () -> assertThat(signUpResponse.id()).isEqualTo(1L),
                () -> assertThat(signUpResponse.accessToken()).isNotBlank()
        );
    }

    @Test
    @DisplayName("중복된 카카오 사용자가 회원가입을 시도하면 예외가 발생한다.")
    void signUpWithDuplicateKakaoIdTest() {
        // given
        Long kakaoId = 1L;
        saveMember(kakaoId);
        SignUpRequest signUpRequest = new SignUpRequest("동백", "동백", "멤버", "2025", List.of("광운대학교"),
                "https://xxx.xxx.xxx");
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(kakaoId,
                new KakaoAccountResponse(new KakaoProfileResponse("동백이", "카카오 이미지 url")));

        // when & then
        assertThatCode(() -> loginService.signUp(signUpRequest, kakaoUserInfo))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("이미 회원가입 된 카카오 유저입니다.");
    }

    @Test
    @DisplayName("올바르지 않은 수의 학교로 회원가입을 시도하면 예외가 발생한다.")
    void signUpWithInvalidUniversitiesCount() {
        // given
        List<String> invalidUniversities = List.of(); // 멤버는 1개의 학교에 소속되어야 한다.
        SignUpRequest signUpRequest = new SignUpRequest("동백", "동백", "멤버", "2025", invalidUniversities,
                "https://xxx.xxx.xxx");
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(1L,
                new KakaoAccountResponse(new KakaoProfileResponse("동백이", "카카오 이미지 url")));

        // when & then
        assertThatCode(() -> loginService.signUp(signUpRequest, kakaoUserInfo))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("소속될 수 있는 학교의 개수가 올바르지 않습니다.");
    }

    private Member saveMember(Long kakaoId) {
        Member member = new Member(kakaoId, "멤버", "동백", "동백", "https://xxx.xxx.xxx", "2025");
        return memberRepository.save(member);
    }
}
