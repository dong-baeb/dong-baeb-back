package com.dongbaeb.demo.auth.service;


import com.dongbaeb.demo.auth.dto.LoginResponse;
import com.dongbaeb.demo.auth.dto.SignUpRequest;
import com.dongbaeb.demo.auth.dto.SignUpResponse;
import com.dongbaeb.demo.auth.dto.kakao.KakaoUserInfo;
import com.dongbaeb.demo.auth.infrastructure.JwtTokenProvider;
import com.dongbaeb.demo.global.exception.BadRequestException;
import com.dongbaeb.demo.profile.entity.Member;
import com.dongbaeb.demo.profile.entity.MemberUniversity;
import com.dongbaeb.demo.profile.entity.University;
import com.dongbaeb.demo.profile.repository.MemberRepository;
import com.dongbaeb.demo.profile.repository.MemberUniversityRepository;
import com.dongbaeb.demo.profile.repository.UniversityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LoginService {
    private final MemberRepository memberRepository;
    private final UniversityRepository universityRepository;
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
        saveMemberUniversity(newMember, signUpRequest.universityId());

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

    private void saveMemberUniversity(Member member, List<Long> universityIds) {
        universityIds.stream()
                .map(this::findUniversity)
                .forEach(university -> memberUniversityRepository.save(new MemberUniversity(member, university)));
    }

    private University findUniversity(Long id) {
        return universityRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("해당 id의 학교가 존재하지 않습니다."));
    }
}
