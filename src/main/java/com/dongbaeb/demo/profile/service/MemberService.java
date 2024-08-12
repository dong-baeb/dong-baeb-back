package com.dongbaeb.demo.profile.service;

import com.dongbaeb.demo.profile.entity.Member;
import com.dongbaeb.demo.profile.entity.University;
import com.dongbaeb.demo.exception.ResourceNotFoundException;
import com.dongbaeb.demo.profile.dto.MemberRequest;
import com.dongbaeb.demo.profile.dto.MemberResponse;
import com.dongbaeb.demo.profile.repository.MemberRepository;
import com.dongbaeb.demo.profile.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final UniversityRepository universityRepository;

    @Transactional
    public MemberResponse createMember(MemberRequest memberRequest) {
        List<University> universities = memberRequest.universityIds().stream()
                .map(universityRepository::findById)
                .map(optionalUniversity -> optionalUniversity.orElseThrow(() -> new ResourceNotFoundException("해당 대학을 찾을 수 없습니다: " + optionalUniversity)))
                .toList();

        Member member = memberRequest.toMember(universities);
        memberRepository.save(member);

        return new MemberResponse(
                member.getId(),
                member.getRole(),
                member.getName(),
                member.getNickname(),
                member.getProfileImageUrl(),
                member.getStudentNo(),
                universities.stream().map(University::getId).toList()
        );
    }

    @Transactional
    public void updateMember(Long id, MemberRequest memberRequest) {
        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 아이디를 가진 사용자를 찾을 수 없습니다: " + id));

        List<University> universities = memberRequest.universityIds().stream()
                .map(universityRepository::findById)
                .map(optionalUniversity -> optionalUniversity.orElseThrow(() -> new ResourceNotFoundException("해당 대학을 찾을 수 없습니다: " + optionalUniversity)))
                .toList();

        Member updateMemberEntity = Member.builder()
                .id(existingMember.getId())
                .kakaoId(memberRequest.kakaoId())
                .role(memberRequest.role())
                .name(memberRequest.name())
                .nickname(memberRequest.nickname())
                .profileImageUrl(memberRequest.profileImageUrl())
                .studentNo(memberRequest.studentNo())
                .universities(universities)
                .build();

        memberRepository.save(updateMemberEntity);

        new MemberResponse(
                updateMemberEntity.getId(),
                updateMemberEntity.getRole(),
                updateMemberEntity.getName(),
                updateMemberEntity.getNickname(),
                updateMemberEntity.getProfileImageUrl(),
                updateMemberEntity.getStudentNo(),
                updateMemberEntity.getUniversities().stream().map(University::getId).toList()
        );
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 아이디를 가진 사용자를 찾을 수 없습니다: " + id));

        memberRepository.delete(member);
    }
}
