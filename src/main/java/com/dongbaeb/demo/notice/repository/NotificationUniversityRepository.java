package com.dongbaeb.demo.notice.repository;

import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.notice.domain.Notice;
import com.dongbaeb.demo.notice.domain.NoticeUniversity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationUniversityRepository extends JpaRepository<NoticeUniversity, Long> {
    Page<NoticeUniversity> findByUniversity(University university, Pageable pageable);

    List<NoticeUniversity> findByNotice(Notice notice);
}
