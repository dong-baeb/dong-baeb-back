package com.dongbaeb.demo;

import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.MemberUniversity;
import com.dongbaeb.demo.member.domain.Role;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.member.repository.MemberRepository;
import com.dongbaeb.demo.member.repository.MemberUniversityRepository;
import com.dongbaeb.demo.notice.domain.Notice;
import com.dongbaeb.demo.notice.domain.NoticeCategory;
import com.dongbaeb.demo.notice.domain.NoticePhoto;
import com.dongbaeb.demo.notice.domain.NoticeUniversity;
import com.dongbaeb.demo.notice.repository.NoticePhotoRepository;
import com.dongbaeb.demo.notice.repository.NoticeRepository;
import com.dongbaeb.demo.notice.repository.NoticeUniversityRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
@RequiredArgsConstructor
public class DataInitialize implements CommandLineRunner {

    private final MemberRepository memberRepository;

    private final MemberUniversityRepository memberUniversityRepository;
    private final NoticeRepository noticeRepository;
    private final NoticeUniversityRepository noticeUniversityRepository;
    private final NoticePhotoRepository noticePhotoRepository;

    @Value("${kakao.id}")
    private Long kakaoId;

    @Override
    public void run(String... args) throws Exception {
        Member member = saveTestMember();
        saveTestEastNotice(member, "동서울 체육대회", "동서울 체육대회 열려요 ㅎ", LocalDate.of(2025, 7, 24), LocalDate.of(2025, 7, 24));
        saveTestUniversityNotice(member, List.of(University.SIRIB, University.SEJONG), "시립 세종 연합 MT",
                "시립 세종 연합 엠티 가요 ㅎㅎ", LocalDate.of(2025, 7, 31), LocalDate.of(2025, 8, 1));
        saveTestEastNotice(member, "동서울 말씀 읽기 모임", "동서울 말씀 읽기 모임 해요.", LocalDate.of(2025, 8, 5),
                LocalDate.of(2025, 8, 8));
    }

    private Member saveTestMember() {
        Member member = new Member(kakaoId, Role.LEADER, "이세영", "전설의아베퍼", "", "2019");
        memberRepository.save(member);
        memberUniversityRepository.save(new MemberUniversity(member, University.SIRIB));
        return member;
    }

    private void saveTestEastNotice(Member author, String title, String content, LocalDate startDate,
                                    LocalDate endDate) {
        Notice notice = new Notice(NoticeCategory.EAST_SEOUL, author, title, content, startDate, endDate);
        noticeRepository.save(notice);
        noticePhotoRepository.save(new NoticePhoto(notice, " "));
    }

    private void saveTestUniversityNotice(Member author, List<University> universities, String title, String content,
                                          LocalDate startDate, LocalDate endDate) {
        Notice notice = new Notice(NoticeCategory.UNIVERSITY, author, title, content, startDate, endDate);
        noticeRepository.save(notice);
        for (University university : universities) {
            noticeUniversityRepository.save(new NoticeUniversity(notice, university));
        }
        noticePhotoRepository.save(new NoticePhoto(notice, " "));
    }
}
