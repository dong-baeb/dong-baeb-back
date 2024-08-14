package com.dongbaeb.demo.auth.controller;

import com.dongbaeb.demo.auth.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/oauth-callback")
    public ResponseEntity<String> login(@RequestParam(name = "code") String code) {
        return ResponseEntity.ok(loginService.login(code));
    }
}
