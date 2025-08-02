package com.dongbaeb.demo.notice.repository;

import java.util.List;
import java.time.LocalDate;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.notice.domain.Notice;
import com.dongbaeb.demo.notice.domain.NoticeCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findAll();

    Page<Notice> findByNoticeCategoryOrderByIdAsc(NoticeCategory noticeCategory, Pageable pageable);

    List<Notice> findByAuthor(Member author);

    List<Notice> findByNoticeCategoryAndStartDateBetween(
            NoticeCategory category,
            LocalDate start,
            LocalDate end
    );

}


