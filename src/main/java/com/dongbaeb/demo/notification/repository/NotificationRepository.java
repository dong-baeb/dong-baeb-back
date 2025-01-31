package com.dongbaeb.demo.notification.repository;

import com.dongbaeb.demo.notification.domain.Notification;
import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    ArrayList<Notification> findAll();

    @Query(value = "SELECT * FROM notification WHERE is_whole = true ORDER BY id ASC LIMIT :limit OFFSET :offset", nativeQuery = true)
    ArrayList<Notification> findPagedWholeEntities(@Param("limit") int limit, @Param("offset") int offset);

}
