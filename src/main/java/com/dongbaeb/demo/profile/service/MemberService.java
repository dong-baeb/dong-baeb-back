package com.dongbaeb.demo.profile.service;

import com.dongbaeb.demo.exception.ResourceNotFoundException;
import com.dongbaeb.demo.profile.dto.MemberRequest;
import com.dongbaeb.demo.profile.dto.MemberResponse;
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
public class MemberService {
    private final MemberRepository memberRepository;
    private final UniversityRepository universityRepository;
    private final MemberUniversityRepository memberUniversityRepository;

    @Transactional
    public MemberResponse createMember(MemberRequest memberRequest) {
        List<University> universities = memberRequest.universityIds().stream()
                .map(universityRepository::findById)
                .map(optionalUniversity -> optionalUniversity.orElseThrow(
                        () -> new ResourceNotFoundException("해당 대학을 찾을 수 없습니다: " + optionalUniversity)))
                .toList();

        Member member = memberRequest.toMember();
        memberRepository.save(member);

        List<MemberUniversity> memberUniversities = universities.stream()
                .map(university -> new MemberUniversity(member, university))
                .toList();
        memberUniversityRepository.saveAll(memberUniversities);

        return MemberResponse.fromMember(member, universities);
    }

    @Transactional
    public void updateMember(Long id, MemberRequest memberRequest) {
        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 아이디를 가진 사용자를 찾을 수 없습니다: " + id));

        List<University> universities = memberRequest.universityIds().stream()
                .map(universityRepository::findById)
                .map(optionalUniversity -> optionalUniversity.orElseThrow(
                        () -> new ResourceNotFoundException("해당 대학을 찾을 수 없습니다: " + optionalUniversity)))
                .toList();

        existingMember.update(memberRequest);
        memberRepository.save(existingMember);

        // 학교명 수정 시 기존 정보 삭제 후 생성
        memberUniversityRepository.deleteByMember(existingMember);

        List<MemberUniversity> memberUniversities = universities.stream()
                .map(university -> new MemberUniversity(existingMember, university))
                .toList();
        memberUniversityRepository.saveAll(memberUniversities);

        MemberResponse.fromMember(existingMember, universities);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 아이디를 가진 사용자를 찾을 수 없습니다: " + id));

        memberRepository.delete(member);
    }
}
