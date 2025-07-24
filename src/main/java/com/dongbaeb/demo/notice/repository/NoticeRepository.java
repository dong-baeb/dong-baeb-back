package com.dongbaeb.demo.notice.repository;


import com.dongbaeb.demo.member.domain.Member;


import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.notice.domain.Notice;
import com.dongbaeb.demo.notice.domain.NoticeCategory;
import com.dongbaeb.demo.notice.domain.NoticeUniversity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findAll();

    Page<Notice> findByNoticeCategoryOrderByIdAsc(NoticeCategory noticeCategory, Pageable pageable);

    List<Notice> findByAuthor(Member author);

    List<Notice> findByNoticeCategory(NoticeCategory noticeCategory);

    List<Notice> findByNoticeCategoryAndStartDateBetween(
            NoticeCategory category,
            LocalDate start,
            LocalDate end
    );

}


