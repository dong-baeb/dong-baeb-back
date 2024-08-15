package com.dongbaeb.demo.profile.repository;

import com.dongbaeb.demo.profile.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}

