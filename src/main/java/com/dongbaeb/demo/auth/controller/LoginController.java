package com.dongbaeb.demo.auth.controller;

import com.dongbaeb.demo.auth.dto.LoginResponse;
import com.dongbaeb.demo.auth.dto.SignUpRequest;
import com.dongbaeb.demo.auth.dto.SignUpResponse;
import com.dongbaeb.demo.auth.dto.kakao.KakaoUserInfo;
import com.dongbaeb.demo.auth.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "로그인 컨트롤러", description = "User에게 Request를 받아 로그인과 관련된 로직을 수행한다.")
@RequiredArgsConstructor
@RestController
public class LoginController {
    private final LoginService loginService;

    @Operation(
            summary = "카카오의 액세스 토큰을 받아 로그인을 수행",
            description = "카카오로 부터 액세스 토큰을 받고 이를 이용하여 로그인을 진행한다."
    )
    @GetMapping("/kakao-login")
    public ResponseEntity<LoginResponse> kakaoLogin(KakaoUserInfo kakaoUserInfo) {
        return ResponseEntity.ok(loginService.kakaoLogin(kakaoUserInfo));
    }

    @Operation(
            summary = "카카오의 액세스 토큰과 회원 정보를 받아 회원가입을 수행",
            description = "카카오의 액세스 토큰과 회원 정보를 받아 회원가입을 진행한다."
    )
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest,
                                                 KakaoUserInfo kakaoUserInfo) {
        return ResponseEntity.ok(loginService.signUp(signUpRequest, kakaoUserInfo));
    }
}
