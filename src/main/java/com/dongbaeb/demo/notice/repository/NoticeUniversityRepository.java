package com.dongbaeb.demo.notice.repository;

import com.dongbaeb.demo.notice.domain.Notice;
import com.dongbaeb.demo.notice.domain.NoticeUniversity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeUniversityRepository extends JpaRepository<NoticeUniversity, Long> {
    List<NoticeUniversity> findByNoticeId(Long noticeId);

    void deleteByNotice(Notice notice);
}