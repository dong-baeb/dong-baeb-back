package com.dongbaeb.demo.notice.repository;

import com.dongbaeb.demo.notice.domain.Notice;
import com.dongbaeb.demo.notice.domain.NoticeCategory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notice, Long> {

    List<Notice> findAll();

//    @Query(value = "SELECT * " +
//            "FROM notice " +
//            "WHERE notice_category = 'EAST_SEOUL' " +
//            "ORDER BY id ASC " +
//            "LIMIT :limit " +
//            "OFFSET :offset",
//            nativeQuery = true)
//    List<Notice> findPagedWholeEntities(@Param("limit") int limit, @Param("offset") int offset);

    Page<Notice> findByNoticeCategoryOrderByIdAsc(NoticeCategory noticeCategory, Pageable pageable);
}
