package com.dongbaeb.demo.auth.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.dongbaeb.demo.auth.dto.LoginResponse;
import com.dongbaeb.demo.auth.dto.SignUpRequest;
import com.dongbaeb.demo.auth.dto.SignUpResponse;
import com.dongbaeb.demo.auth.dto.kakao.KakaoAccountResponse;
import com.dongbaeb.demo.auth.dto.kakao.KakaoProfileResponse;
import com.dongbaeb.demo.auth.dto.kakao.KakaoUserInfo;
import com.dongbaeb.demo.auth.infrastructure.JwtTokenProvider;
import com.dongbaeb.demo.auth.infrastructure.KakaoOauthClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

// TODO: 테스트 격리(데이터베이스 초기화)
@TestPropertySource(properties = {"spring.config.location = classpath:test-application.yml"})
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class LoginControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    JwtTokenProvider jwtTokenProvider;
    @MockBean
    KakaoOauthClient kakaoOauthClient;
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("카카오 로그인을 수행한다.")
    void kakaoLoginTest() throws Exception {
        when(kakaoOauthClient.requestUserInfo(any()))
                .thenReturn(new KakaoUserInfo(1L, new KakaoAccountResponse(new KakaoProfileResponse("동백", "동백"))));
        LoginResponse loginResponse = new LoginResponse(null, false);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + "kakaoAccessToken")
                .when().post("/kakao-login")
                .then().log().all()
                .statusCode(200)
                .body(is(objectMapper.writeValueAsString(loginResponse)));
    }

    @Test
    @DisplayName("회원가입을 수행한다.")
    void signUpTest() throws Exception {
        String accessToken = "JwtAccessToken";
        when(kakaoOauthClient.requestUserInfo(any()))
                .thenReturn(new KakaoUserInfo(2L, new KakaoAccountResponse(new KakaoProfileResponse("동백", "동백"))));
        when(jwtTokenProvider.createAccessToken(any()))
                .thenReturn(accessToken);
        SignUpRequest signUpRequest =
                new SignUpRequest("동백", "동백", "멤버", "2025", List.of("광운대학교"), "https://xxx.xxx.xxx");
        SignUpResponse signUpResponse = new SignUpResponse(1L, accessToken);

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + "kakaoAccessToken")
                .contentType(ContentType.JSON)
                .body(signUpRequest)
                .when().post("/sign-up")
                .then().log().all()
                .statusCode(200)
                .body(is(objectMapper.writeValueAsString(signUpResponse)));
    }
}
