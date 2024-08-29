package com.dongbaeb.demo.profile.repository;

import com.dongbaeb.demo.profile.entity.Member;
import com.dongbaeb.demo.profile.entity.MemberUniversity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberUniversityRepository extends JpaRepository<MemberUniversity, Long> {
    List<MemberUniversity> findByMember(Member member);

    void deleteByMember(Member member);
}
