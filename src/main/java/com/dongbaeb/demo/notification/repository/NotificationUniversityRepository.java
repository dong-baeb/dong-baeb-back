package com.dongbaeb.demo.notification.repository;

import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.notification.domain.NotificationUniversity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface NotificationUniversityRepository extends JpaRepository<NotificationUniversity,Long> {
    ArrayList<NotificationUniversity> findByUniversity(University university);
}
