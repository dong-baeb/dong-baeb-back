package com.dongbaeb.demo.notice.repository;

import com.dongbaeb.demo.notice.domain.NoticePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticePhotoRepository extends JpaRepository<NoticePhoto, Long> {
}
