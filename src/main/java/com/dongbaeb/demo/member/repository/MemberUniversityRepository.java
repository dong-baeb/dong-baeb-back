package com.dongbaeb.demo.member.repository;

import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.MemberUniversity;
import com.dongbaeb.demo.member.domain.University;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberUniversityRepository extends JpaRepository<MemberUniversity, Long> {
    Optional<MemberUniversity> findByMember(Member member);

    boolean existsByMemberAndUniversity(Member member, University university);

    boolean existsAllByMemberAndUniversityIn(Member member, List<University> university);

    void deleteByMember(Member member);
}
