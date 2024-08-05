package com.dongbaeb.demo.auth.service;


import com.dongbaeb.demo.auth.dto.KakaoAccessTokenResponse;
import com.dongbaeb.demo.auth.infrastructure.KakaoOauthClient;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final KakaoOauthClient kakaoOauthClient;

    public LoginService(KakaoOauthClient kakaoOauthClient) {
        this.kakaoOauthClient = kakaoOauthClient;
    }

    public KakaoAccessTokenResponse login(String authorizationCode) {
        return kakaoOauthClient.requestAccessToken(authorizationCode);
    }
}
