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
<<<<<<< HEAD
=======

    @Query("select mu.university from MemberUniversity mu where mu.member = :member")
    List<University> findUniversitiesByMember(@Param("member") Member member);
>>>>>>> 09278221e768ea2c410c59f06234d8d191f97fee

    boolean existsByMemberAndUniversity(Member member, University university);

    void deleteByMember(Member member);
}
