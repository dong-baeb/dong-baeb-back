package com.dongbaeb.demo.notification.repository;

import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.notification.domain.NoticeUniversity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationUniversityRepository extends JpaRepository<NoticeUniversity, Long> {
    List<NoticeUniversity> findByUniversity(University university);
}
