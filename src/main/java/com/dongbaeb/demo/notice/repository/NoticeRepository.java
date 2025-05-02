package com.dongbaeb.demo.notice.repository;

import com.dongbaeb.demo.notice.domain.Notice;
import com.dongbaeb.demo.notice.domain.NoticeCategory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findAll();

    Page<Notice> findByNoticeCategoryOrderByIdAsc(NoticeCategory noticeCategory, Pageable pageable);
}
