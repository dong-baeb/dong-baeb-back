package com.dongbaeb.demo.notification.repository;

import com.dongbaeb.demo.notification.domain.Notice;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notice, Long> {

    List<Notice> findAll();

    @Query(value = "SELECT * " +
            "FROM notice " +
            "WHERE notice_category = 'EAST_SEOUL' " +
            "ORDER BY id ASC " +
            "LIMIT :limit " +
            "OFFSET :offset",
            nativeQuery = true)
    List<Notice> findPagedWholeEntities(@Param("limit") int limit, @Param("offset") int offset);
}
