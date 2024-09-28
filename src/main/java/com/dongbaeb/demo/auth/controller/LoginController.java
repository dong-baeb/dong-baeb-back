package com.dongbaeb.demo.auth.controller;

import com.dongbaeb.demo.auth.dto.LoginResponse;
import com.dongbaeb.demo.auth.dto.SignUpRequest;
import com.dongbaeb.demo.auth.dto.SignUpResponse;
import com.dongbaeb.demo.auth.dto.kakao.KakaoUserInfo;
import com.dongbaeb.demo.auth.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/kakao-login")
    public ResponseEntity<LoginResponse> kakaoLogin(KakaoUserInfo kakaoUserInfo) {
        return ResponseEntity.ok(loginService.kakaoLogin(kakaoUserInfo));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest,
                                                 KakaoUserInfo kakaoUserInfo) {
        return ResponseEntity.ok(loginService.signUp(signUpRequest, kakaoUserInfo));
    }
}
