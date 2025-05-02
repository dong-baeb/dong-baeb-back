package com.dongbaeb.demo.notice.repository;

import com.dongbaeb.demo.notice.domain.Notice;
import com.dongbaeb.demo.notice.domain.NoticePhoto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticePhotoRepository extends JpaRepository<NoticePhoto, Long> {
    List<NoticePhoto> findByNotice(Notice notice);
}
