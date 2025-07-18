package com.dongbaeb.demo.member.repository;

import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.MemberUniversity;
import com.dongbaeb.demo.member.domain.University;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberUniversityRepository extends JpaRepository<MemberUniversity, Long> {
    List<MemberUniversity> findByMember(Member member);

    @Query("select mu.university from MemberUniversity mu where mu.member = :member")
    List<University> findUniversitiesByMember(@Param("member") Member member);

    boolean existsByMemberAndUniversity(Member member, University university);

    void deleteByMember(Member member);
}
