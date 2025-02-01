package com.dongbaeb.demo.member.controller;

import com.dongbaeb.demo.auth.infrastructure.JwtTokenProvider;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.dto.MemberRequest;
import com.dongbaeb.demo.member.repository.MemberRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {"spring.config.location = classpath:test-application.yml"})
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class MemberControllerTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("멤버를 업데이트 한다.")
    void updateMemberTest() {
        Member member = saveMember();
        MemberRequest memberRequest =
                new MemberRequest("은백", "은백", "멤버", "2025", List.of("광운대학교"), "https://xxx.xxx.xxx");

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + createToken(member))
                .contentType(ContentType.JSON)
                .body(memberRequest)
                .when().put("/members/" + member.getId())
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("멤버를 삭제 한다.")
    void deleteMemberTest() {
        Member member = saveMember();

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + createToken(member))
                .when().delete("/members/" + member.getId())
                .then().log().all()
                .statusCode(204);
    }

    private Member saveMember() {
        Member member = new Member(1L, "멤버", "동백", "동백", "https://xxx.xxx.xxx", "2025");
        return memberRepository.save(member);
    }

    private String createToken(Member member) {
        return jwtTokenProvider.createAccessToken(member.getId());
    }
}
