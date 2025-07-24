package com.dongbaeb.demo.notice.repository;

import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.notice.domain.Notice;
import com.dongbaeb.demo.notice.domain.NoticeUniversity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeUniversityRepository extends JpaRepository<NoticeUniversity, Long> {
    Page<NoticeUniversity> findByUniversity(University university, Pageable pageable);

    List<NoticeUniversity> findByNotice(Notice notice);

    List<NoticeUniversity> findByNoticeId(Long noticeId);

    @Query("""
                SELECT nu FROM NoticeUniversity nu
                JOIN FETCH nu.notice n
                WHERE nu.university IN :universities
                  AND n.startDate BETWEEN :start AND :end
            """)
    List<NoticeUniversity> findByUniversitiesAndDateRange(
            @Param("universities") List<University> universities,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );


    void deleteByNotice(Notice notice);

}
