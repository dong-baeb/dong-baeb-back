package com.dongbaeb.demo.profile.service;

import com.dongbaeb.demo.profile.entity.Member;
import com.dongbaeb.demo.profile.entity.University;
import com.dongbaeb.demo.exception.ResourceNotFoundException;
import com.dongbaeb.demo.profile.dto.MemberRequest;
import com.dongbaeb.demo.profile.dto.MemberResponse;
import com.dongbaeb.demo.profile.repository.MemberRepository;
import com.dongbaeb.demo.profile.repository.UniversityRepository;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final UniversityRepository universityRepository;

    public MemberService(MemberRepository memberRepository, UniversityRepository universityRepository) {
        this.memberRepository = memberRepository;
        this.universityRepository = universityRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = new Member();
        member.setKakaoId(memberRequest.kakaoId());
        member.setRole(memberRequest.role());
        member.setName(memberRequest.name());
        member.setNickname(memberRequest.nickname());
        member.setProfileImageUrl(memberRequest.profileImageUrl());
        member.setStudentNo(memberRequest.studentNo());

        List<University> universities = memberRequest.universityIds().stream()
                .map(universityRepository::findById)
                .map(optionalUniversity -> optionalUniversity.orElseThrow(() -> new ResourceNotFoundException("해당 대학을 찾을 수 없습니다: " + optionalUniversity)))
                .toList();

        member.setUniversities(universities);
        memberRepository.save(member);

        return new MemberResponse(
                member.getId(),
                member.getKakaoId(),
                member.getRole(),
                member.getName(),
                member.getNickname(),
                member.getProfileImageUrl(),
                member.getStudentNo(),
                universities.stream().map(University::getId).collect(Collectors.toList())
        );
    }

    @Transactional
    public MemberResponse updateMember(Long id, MemberRequest memberRequest) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 아이디를 가진 사용자를 찾을 수 없습니다: " + id));

        member.setName(memberRequest.name());
        member.setRole(memberRequest.role());
        member.setNickname(memberRequest.nickname());
        member.setProfileImageUrl(memberRequest.profileImageUrl());
        member.setStudentNo(memberRequest.studentNo());

        Member updatedMember = memberRepository.save(member);
        return new MemberResponse(updatedMember);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 아이디를 가진 사용자를 찾을 수 없습니다: " + id));

        memberRepository.delete(member);
    }
}

