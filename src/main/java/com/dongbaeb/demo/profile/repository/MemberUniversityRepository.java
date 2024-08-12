package com.dongbaeb.demo.profile.repository;

import com.dongbaeb.demo.profile.entity.Member;
import com.dongbaeb.demo.profile.entity.MemberUniversity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberUniversityRepository extends JpaRepository<MemberUniversity, Long> {
    List<MemberUniversity> findByMember(Member member);
    void deleteByMember(Member member);
}
