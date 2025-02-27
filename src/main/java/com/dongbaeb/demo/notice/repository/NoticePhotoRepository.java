package com.dongbaeb.demo.notice.repository;

import com.dongbaeb.demo.notice.domain.Notice;
import com.dongbaeb.demo.notice.domain.NoticePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticePhotoRepository extends JpaRepository<NoticePhoto, Long> {
    List<NoticePhoto> findByNoticeId(Long noticeId);

    void deleteByNotice(Notice notice);
}
