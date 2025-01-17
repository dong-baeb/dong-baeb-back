package com.dongbaeb.demo.member.service;

import com.dongbaeb.demo.global.dto.MemberAuth;
import com.dongbaeb.demo.global.exception.BadRequestException;
import com.dongbaeb.demo.global.exception.ForbiddenException;
import com.dongbaeb.demo.global.exception.ResourceNotFoundException;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.MemberUniversity;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.member.dto.MemberRequest;
import com.dongbaeb.demo.member.dto.MemberResponse;
import com.dongbaeb.demo.member.repository.MemberRepository;
import com.dongbaeb.demo.member.repository.MemberUniversityRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberUniversityRepository memberUniversityRepository;

    @Transactional
    public void updateMember(Long id, MemberRequest memberRequest, MemberAuth memberAuth) {
        validateAuthority(id, memberAuth);
        Member member = findMember(id);
        List<University> universities = findUniversities(memberRequest.universities());

        member.update(memberRequest.role(), memberRequest.name(), memberRequest.nickname(), memberRequest.studentNo());
        updateMemberUniversities(member, universities);

        MemberResponse.fromMember(member, universities);
    }

    private void updateMemberUniversities(Member member, List<University> universities) {
        validateUniversitiesCount(member, universities);
        memberUniversityRepository.deleteByMember(member);
        universities.forEach(university -> memberUniversityRepository.save(new MemberUniversity(member, university)));
    }

    private void validateUniversitiesCount(Member member, List<University> universities) {
        if (!member.isValidUniversitiesCount(universities.size())) {
            throw new BadRequestException("소속될 수 있는 학교의 개수가 올바르지 않습니다.");
        }
    }

    @Transactional
    public void deleteMember(Long id, MemberAuth memberAuth) {
        validateAuthority(id, memberAuth);
        memberRepository.delete(findMember(id));
    }

    private void validateAuthority(Long id, MemberAuth memberAuth) {
        if (!Objects.equals(memberAuth.memberId(), id)) {
            throw new ForbiddenException("권한이 없습니다.");
        }
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 아이디를 가진 사용자를 찾을 수 없습니다: " + id));
    }

    private List<University> findUniversities(List<String> universities) {
        return universities.stream()
                .map(University::fromName)
                .toList();
    }
}
