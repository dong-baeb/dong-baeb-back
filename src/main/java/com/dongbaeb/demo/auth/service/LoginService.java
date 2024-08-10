package com.dongbaeb.demo.auth.service;


import com.dongbaeb.demo.auth.dto.kakao.KakaoUserInfoResponse;
import com.dongbaeb.demo.auth.infrastructure.JwtTokenProvider;
import com.dongbaeb.demo.auth.infrastructure.KakaoOauthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService {
    private final JwtTokenProvider jwtProvider;
    private final KakaoOauthClient kakaoOauthClient;

    public String login(String authorizationCode) {
        String accessToken = kakaoOauthClient.requestAccessToken(authorizationCode);
        KakaoUserInfoResponse kakaoUserInfoResponse = kakaoOauthClient.requestUserInfo(accessToken);

        return jwtProvider.createAccessToken(kakaoUserInfoResponse.kakaoId());
    }
}
