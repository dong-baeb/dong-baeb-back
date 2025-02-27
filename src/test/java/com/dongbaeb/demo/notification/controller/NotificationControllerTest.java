package com.dongbaeb.demo.notification.controller;

import com.dongbaeb.demo.auth.infrastructure.JwtTokenProvider;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.repository.MemberRepository;
import com.dongbaeb.demo.notification.dto.NotificationRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
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
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class NotificationControllerTest {
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
    @DisplayName("공지를 작성한다.")
    void createNotificationTest() {
        Member member = saveMember();
        NotificationRequest notificationRequest =
                new NotificationRequest("동서울", "제목", "내용", LocalDate.now(), LocalDate.now(), List.of("url"), List.of());

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + createToken(member))
                .contentType(ContentType.JSON)
                .body(notificationRequest)
                .when().post("/notifications")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/notifications/1");
    }

    private Member saveMember() {
        Member member = new Member(1L, "간사", "동백", "동백", "https://xxx.xxx.xxx", "2025");
        return memberRepository.save(member);
    }

    private String createToken(Member member) {
        return jwtTokenProvider.createAccessToken(member.getId());
    }
}
