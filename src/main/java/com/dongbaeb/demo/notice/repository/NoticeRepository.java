package com.dongbaeb.demo.notice.repository;

<<<<<<< HEAD
=======
import com.dongbaeb.demo.member.domain.Member;

>>>>>>> 09278221e768ea2c410c59f06234d8d191f97fee
import com.dongbaeb.demo.notice.domain.Notice;
import com.dongbaeb.demo.notice.domain.NoticeCategory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findAll();

    Page<Notice> findByNoticeCategoryOrderByIdAsc(NoticeCategory noticeCategory, Pageable pageable);

<<<<<<< HEAD
=======
    List<Notice> findByAuthor(Member author);
>>>>>>> 09278221e768ea2c410c59f06234d8d191f97fee

}


