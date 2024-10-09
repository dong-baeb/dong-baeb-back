package com.dongbaeb.demo.profile.service;

import com.dongbaeb.demo.global.exception.ResourceNotFoundException;
import com.dongbaeb.demo.profile.dto.MemberRequest;
import com.dongbaeb.demo.profile.dto.MemberResponse;
import com.dongbaeb.demo.profile.entity.Member;
import com.dongbaeb.demo.profile.entity.MemberUniversity;
import com.dongbaeb.demo.profile.entity.University;
import com.dongbaeb.demo.profile.repository.MemberRepository;
import com.dongbaeb.demo.profile.repository.MemberUniversityRepository;
import com.dongbaeb.demo.profile.repository.UniversityRepository;
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
    private final UniversityRepository universityRepository;
    private final MemberUniversityRepository memberUniversityRepository;

    @Transactional
    public void updateMember(Long id, MemberRequest memberRequest) {
        Member member = findMember(id);
        List<University> universities = findUniversities(memberRequest.universityIds());

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

    private List<University> findUniversities(List<Long> universityIds) {
        return universityIds.stream()
                .map(this::findUniversity)
                .toList();
    }

    private University findUniversity(Long id) {
        return universityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 아이디를 가진 대학을 찾을 수 없습니다: " + id));
    }
}
