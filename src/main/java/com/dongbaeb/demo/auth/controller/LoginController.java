package com.dongbaeb.demo.auth.controller;

import com.dongbaeb.demo.auth.dto.LoginResponse;
import com.dongbaeb.demo.auth.dto.SignUpRequest;
import com.dongbaeb.demo.auth.dto.SignUpResponse;
import com.dongbaeb.demo.auth.dto.kakao.KakaoUserInfo;
import com.dongbaeb.demo.auth.service.LoginService;
import com.dongbaeb.demo.global.exception.dto.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "로그인", description = "로그인 및 회원가입 API")
@RequiredArgsConstructor
@RestController
public class LoginController {
    private final LoginService loginService;

    @Operation(
            summary = "카카오 로그인",
            description = "카카오의 액세스 토큰을 받아 로그인을 수행"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않은 카카오 액세스 토큰으로 인한 로그인 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @PostMapping("/kakao-login")
    public ResponseEntity<LoginResponse> kakaoLogin(KakaoUserInfo kakaoUserInfo) {
        return ResponseEntity.ok(loginService.kakaoLogin(kakaoUserInfo));
    }

    @Operation(
            summary = "회원가입",
            description = "카카오의 액세스 토큰과 회원 정보를 받아 회원가입을 수행"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원가입 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "올바르지 않은 요청으로 인한 회원가입 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않은 카카오 액세스 토큰으로 인한 회원가입 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest,
                                                 KakaoUserInfo kakaoUserInfo) {
        return ResponseEntity.ok(loginService.signUp(signUpRequest, kakaoUserInfo));
    }
}
