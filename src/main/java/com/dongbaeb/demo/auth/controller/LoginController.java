package com.dongbaeb.demo.auth.controller;

import com.dongbaeb.demo.auth.dto.LoginResponse;
import com.dongbaeb.demo.auth.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/kakao-login")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(loginService.kakaoLogin(authorizationHeader));
    }
}
