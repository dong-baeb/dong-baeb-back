package com.dongbaeb.demo.auth.service;


import com.dongbaeb.demo.auth.dto.LoginResponse;
import com.dongbaeb.demo.auth.dto.kakao.KakaoUserInfoResponse;
import com.dongbaeb.demo.auth.infrastructure.JwtTokenExtractor;
import com.dongbaeb.demo.auth.infrastructure.JwtTokenProvider;
import com.dongbaeb.demo.auth.infrastructure.KakaoOauthClient;
import com.dongbaeb.demo.profile.entity.Member;
import com.dongbaeb.demo.profile.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService {
    private final MemberRepository memberRepository;
    private final JwtTokenExtractor jwtTokenExtractor;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoOauthClient kakaoOauthClient;

    public LoginResponse kakaoLogin(String authorizationHeader) {
        String kakaoAccessToken = jwtTokenExtractor.extractToken(authorizationHeader);
        KakaoUserInfoResponse kakaoUserInfoResponse = kakaoOauthClient.requestUserInfo(kakaoAccessToken);

        return memberRepository.findByKakaoId(kakaoUserInfoResponse.kakaoId())
                .map(member -> LoginResponse.createRegistered(getAccessToken(member)))
                .orElseGet(LoginResponse::createNotRegistered);
    }

    private String getAccessToken(Member member) {
        return jwtTokenProvider.createAccessToken(member.getId());
    }
}
