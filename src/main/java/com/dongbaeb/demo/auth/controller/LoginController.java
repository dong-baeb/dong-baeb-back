package com.dongbaeb.demo.auth.controller;

import com.dongbaeb.demo.auth.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "로그인 컨트롤러", description = "User에게 Request를 받아 로그인과 관련된 로직을 수행한다.")
@RequiredArgsConstructor
@RestController
public class LoginController {
    private final LoginService loginService;

    @Operation(summary = "code를 받아 로그인을 수행", description = "Kakao로 부터 인가 code를 받고 이를 이용하여 로그인을 진행한다.")
    @GetMapping("/oauth-callback")
    public ResponseEntity<String> login(
            @Parameter(description = "Kakao로 부터 받은 인가 code")
            @RequestParam(name = "code") String code) {
        return ResponseEntity.ok(loginService.login(code));
    }
}
