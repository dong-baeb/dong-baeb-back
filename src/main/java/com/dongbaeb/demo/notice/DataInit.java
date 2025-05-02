package com.dongbaeb.demo.notice;


import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.MemberUniversity;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.member.repository.MemberRepository;
import com.dongbaeb.demo.member.repository.MemberUniversityRepository;
import com.dongbaeb.demo.notice.domain.Notice;
import com.dongbaeb.demo.notice.domain.NoticePhoto;
import com.dongbaeb.demo.notice.domain.NoticeUniversity;
import com.dongbaeb.demo.notice.repository.NoticePhotoRepository;
import com.dongbaeb.demo.notice.repository.NoticeRepository;
import com.dongbaeb.demo.notice.repository.NoticeUniversityRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class DataInit implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final NoticeRepository noticeRepository;

    private final MemberUniversityRepository memberUniversityRepository;

    private final NoticePhotoRepository noticePhotoRepository;

    private final NoticeUniversityRepository noticeUniversityRepository;

    @Override
    public void run(String... args) {
        Member member;
        Notice notice;
        member = memberRepository.save(new Member(32532523L, "간사", "이세영", "쿄쿄", "fegweg", "ggs"));
        memberUniversityRepository.save(new MemberUniversity(member, University.SIRIB));
        notice = noticeRepository.save(
                new Notice("학교", member, "안녕", "반가워", LocalDate.of(2025, 2, 10), LocalDate.of(2025, 2, 15)));
        noticePhotoRepository.save(new NoticePhoto(notice, "/123"));
        noticeUniversityRepository.save(new NoticeUniversity(notice, University.SIRIB));

        memberRepository.save(new Member(26265325L, "멤버", "이세웅", "코콬", "sgsg", "sgsdgsd"));
        member = memberRepository.save(new Member(25325L, "간사", "권민우", "캬캬", "sgsgsdg", "sgsdgsds"));
        notice = noticeRepository.save(
                new Notice("동서울", member, "안녕", "반가워", LocalDate.of(2025, 2, 10), LocalDate.of(2025, 2, 15)));
        noticePhotoRepository.save(new NoticePhoto(notice, "/456"));
        noticePhotoRepository.save(new NoticePhoto(notice, "/126"));

        for (int i = 0; i < 50; i++) {
            if (i % 5 == 0) {
                notice = noticeRepository.save(
                        new Notice("학교", member, "안녕" + String.valueOf(i), "반가워", LocalDate.of(2025, 2, 10),
                                LocalDate.of(2025, 2, 15)));
                noticeUniversityRepository.save(new NoticeUniversity(notice, University.KONKUK));
                noticePhotoRepository.save(new NoticePhoto(notice, "/547"));
            } else {
                notice = noticeRepository.save(
                        new Notice("동서울", member, "안녕" + String.valueOf(i), "반가워", LocalDate.of(2025, 2, 10),
                                LocalDate.of(2025, 2, 15)));
                noticePhotoRepository.save(new NoticePhoto(notice, "/"));
            }
        }
    }
}
