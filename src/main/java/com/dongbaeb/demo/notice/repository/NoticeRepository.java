package com.dongbaeb.demo.notice.repository;

import com.dongbaeb.demo.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
