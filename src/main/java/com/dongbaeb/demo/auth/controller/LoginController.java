package com.dongbaeb.demo.auth.controller;

import com.dongbaeb.demo.auth.dto.KakaoAccessTokenResponse;
import com.dongbaeb.demo.auth.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/oauth-callback")
    public ResponseEntity<KakaoAccessTokenResponse> login(@RequestParam(name = "code") String code) {
        return ResponseEntity.ok(loginService.login(code));
    }
}
