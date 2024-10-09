package com.dongbaeb.demo.profile.repository;

import com.dongbaeb.demo.profile.entity.Member;
import com.dongbaeb.demo.profile.entity.MemberUniversity;
import com.dongbaeb.demo.profile.entity.University;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberUniversityRepository extends JpaRepository<MemberUniversity, Long> {
    Optional<MemberUniversity> findByMember(Member member);

    boolean existsByMemberAndUniversity(Member member, University university);

    void deleteByMemberAndUniversity(Member member, University university);
}
