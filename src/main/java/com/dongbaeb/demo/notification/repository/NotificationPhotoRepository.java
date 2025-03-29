package com.dongbaeb.demo.notification.repository;

import com.dongbaeb.demo.notification.domain.Notice;
import com.dongbaeb.demo.notification.domain.NoticePhoto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationPhotoRepository extends JpaRepository<NoticePhoto, Long> {

    List<NoticePhoto> findByNotice(Notice notice);
}
