package com.dongbaeb.demo.auth.service;


import com.dongbaeb.demo.auth.dto.kakao.KakaoUserInfoResponse;
import com.dongbaeb.demo.auth.infrastructure.JwtTokenExtractor;
import com.dongbaeb.demo.auth.infrastructure.JwtTokenProvider;
import com.dongbaeb.demo.auth.infrastructure.KakaoOauthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService {
    private final JwtTokenExtractor jwtTokenExtractor;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoOauthClient kakaoOauthClient;

    public String kakaoLogin(String authorizationHeader) {
        String kakaoAccessToken = jwtTokenExtractor.extractToken(authorizationHeader);
        KakaoUserInfoResponse kakaoUserInfoResponse = kakaoOauthClient.requestUserInfo(kakaoAccessToken);

        System.out.println(kakaoUserInfoResponse.kakaoAccount().kakaoProfile().nickname());
        return jwtTokenProvider.createAccessToken(kakaoUserInfoResponse.kakaoId());
    }
}
