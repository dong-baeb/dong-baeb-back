package com.dongbaeb.demo.member.service;

import com.dongbaeb.demo.global.exception.ResourceNotFoundException;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.MemberUniversity;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.member.dto.MemberRequest;
import com.dongbaeb.demo.member.dto.MemberResponse;
import com.dongbaeb.demo.member.repository.MemberRepository;
import com.dongbaeb.demo.member.repository.MemberUniversityRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberUniversityRepository memberUniversityRepository;

    @Transactional
    public void updateMember(Long id, MemberRequest memberRequest) {
        Member member = findMember(id);
        List<University> universities = findUniversities(memberRequest.universities());

        member.update(memberRequest);
        updateMemberUniversities(member, universities);

        MemberResponse.fromMember(member, universities);
    }

    // TODO: 리팩토링 필요. 양방향 고려.
    private void updateMemberUniversities(Member member, List<University> universities) {
        Set<University> universitiesSet = new HashSet<>(universities);

        memberUniversityRepository.findByMember(member).stream()
                .filter(memberUniversity -> !universitiesSet.contains(memberUniversity.getUniversity()))
                .forEach(memberUniversityRepository::delete);

        universities.stream()
                .filter(university -> !memberUniversityRepository.existsByMemberAndUniversity(member, university))
                .forEach(university -> memberUniversityRepository.save(new MemberUniversity(member, university)));
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.delete(findMember(id));
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
