package com.dongbaeb.demo.auth.service;


import com.dongbaeb.demo.auth.dto.LoginResponse;
import com.dongbaeb.demo.auth.dto.SignUpRequest;
import com.dongbaeb.demo.auth.dto.SignUpResponse;
import com.dongbaeb.demo.auth.dto.kakao.KakaoUserInfo;
import com.dongbaeb.demo.auth.infrastructure.JwtTokenProvider;
import com.dongbaeb.demo.global.exception.BadRequestException;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.MemberUniversity;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.member.repository.MemberRepository;
import com.dongbaeb.demo.member.repository.MemberUniversityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LoginService {
    private final MemberRepository memberRepository;
    private final MemberUniversityRepository memberUniversityRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public LoginResponse kakaoLogin(KakaoUserInfo kakaoUserInfo) {
        return memberRepository.findByKakaoId(kakaoUserInfo.kakaoId())
                .map(member -> LoginResponse.createRegistered(getAccessToken(member)))
                .orElseGet(LoginResponse::createNotRegistered);
    }

    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest, KakaoUserInfo kakaoUserInfo) {
        Long kakaoId = kakaoUserInfo.kakaoId();
        validateDuplicateByKakaoId(kakaoId);
        Member newMember = memberRepository.save(signUpRequest.toMember(kakaoId));
        saveMemberUniversity(newMember, signUpRequest.universities());

        return SignUpResponse.from(newMember, getAccessToken(newMember));
    }

    private String getAccessToken(Member member) {
        return jwtTokenProvider.createAccessToken(member.getId());
    }

    private void validateDuplicateByKakaoId(Long kakaoId) {
        if (memberRepository.findByKakaoId(kakaoId).isPresent()) {
            throw new BadRequestException("이미 회원가입 된 카카오 유저입니다.");
        }
    }

    private void saveMemberUniversity(Member member, List<String> universities) {
        universities.stream()
                .map(University::fromName)
                .forEach(university -> memberUniversityRepository.save(new MemberUniversity(member, university)));
    }
}
