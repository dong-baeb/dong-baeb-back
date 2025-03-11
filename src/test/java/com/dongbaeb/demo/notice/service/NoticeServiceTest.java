package com.dongbaeb.demo.notice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dongbaeb.demo.global.dto.MemberAuth;
import com.dongbaeb.demo.global.exception.ForbiddenException;
import com.dongbaeb.demo.member.domain.MemberUniversity;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.member.repository.MemberRepository;
import com.dongbaeb.demo.member.repository.MemberUniversityRepository;
import com.dongbaeb.demo.notice.domain.Notice;
import com.dongbaeb.demo.notice.domain.NoticePhoto;
import com.dongbaeb.demo.notice.domain.NoticeUniversity;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.notice.dto.NoticeResponse;
import com.dongbaeb.demo.notice.repository.NoticePhotoRepository;
import com.dongbaeb.demo.notice.repository.NoticeRepository;
import com.dongbaeb.demo.notice.repository.NoticeUniversityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@Import({NoticeService.class})
@TestPropertySource(properties = {"spring.config.location = classpath:test-application.yml"})
class NoticeServiceTest {
    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberUniversityRepository memberUniversityRepository;
    @Autowired
    NoticeService noticeService;
    @Autowired
    NoticePhotoRepository noticePhotoRepository;
    @Autowired
    NoticeUniversityRepository noticeUniversityRepository;

    @Test
    @DisplayName("공지를 정상적으로 조회한다.")
    void readNoticeTest() {
        // given
        Member author = saveMember("리더");
        Notice notice = saveTestNotice(author);
        saveMemberUniversity(author, University.KONKUK);

        // when
        NoticeResponse response = noticeService.readNotice(notice.getId(), new MemberAuth(author.getId()));

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(notice.getId());
    }

    @Test
    @DisplayName("다른 학교의 공지 조회 시 예외가 발생한다.")
    void readNoticeValidateUniversityExceptionTest() {
        // given
        Member author = saveMember("리더");
        Notice notice = saveTestNotice(author);
        Member member = new Member(2L, "멤버", "동백2", "동백2", "url", "2025");
        memberRepository.save(member);
        saveMemberUniversity(member, University.SIRIB);

        // when & then
        assertThatThrownBy(() -> noticeService.readNotice(notice.getId(), new MemberAuth(member.getId())))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("공지 조회 권한이 없습니다.");
    }

    @Test
    @DisplayName("간사는 자신의 소속 학교가 아닌 공지를 조회할 수 있다.")
    void readNoticeValidateMissionaryAccessTest() {
        // given
        Member author = saveMember("리더");
        Notice notice = saveTestNotice(author);
        Member missionary = new Member(2L, "간사", "동백2", "동백2", "url", "2025");
        memberRepository.save(missionary);
        saveMemberUniversity(missionary, University.SIRIB);

        // when
        NoticeResponse response = noticeService.readNotice(notice.getId(), new MemberAuth(missionary.getId()));

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(notice.getId());
    }

    @Test
    @DisplayName("멤버가 공지를 삭제하려는 경우 예외가 발생한다.")
    void deleteNoticeUnValidateMemberExceptionTest() {
        // given
        Member author = saveMember("리더");
        Notice notice = saveTestNotice(author);
        Member member = new Member(2L, "멤버", "동백2", "동백2", "url", "2025");
        memberRepository.save(member);

        // when & then
        assertThatThrownBy(() -> noticeService.deleteNotice(notice.getId(), new MemberAuth(member.getId())))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("공지 삭제 권한이 없습니다.");
    }

    @Test
    @DisplayName("리더는 자신이 소속된 학교의 공지를 정상적으로 삭제할 수 있다.")
    void deleteNoticeWithLeaderBelongToUniversityTest() {
        // given
        Member author = saveMember("리더");
        Notice notice = saveTestNotice(author);
        Member leader = new Member(2L, "리더", "동백2", "동백2", "url", "2025");
        memberRepository.save(leader);
        saveMemberUniversity(leader, University.KONKUK);

        // when
        noticeService.deleteNotice(notice.getId(), new MemberAuth(leader.getId()));

        // then
        assertThat(noticeRepository.existsById(notice.getId())).isFalse();
    }

    @Test
    @DisplayName("리더가 자신이 소속되지 않는 학교의 공지를 삭제하려는 경우 예외가 발생한다.")
    void deleteNoticeWithLeaderNotBelongToUniversityExceptionTest() {
        // given
        Member author = saveMember("리더");
        Notice notice = saveTestNotice(author);
        Member leader = new Member(2L, "리더", "동백2", "동백2", "url", "2025");
        memberRepository.save(leader);
        saveMemberUniversity(leader, University.SIRIB);

        // when & then
        assertThatThrownBy(() -> noticeService.deleteNotice(notice.getId(), new MemberAuth(leader.getId())))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("공지 삭제 권한이 없습니다.");
    }

    @Test
    @DisplayName("간사는 자신이 소속되지 않는 학교의 공지를 삭제할 수 있다.")
    void deleteNoticeWithMissionaryNotBelongToUniversityTest() {
        // given
        Member author = saveMember("리더");
        Notice notice = saveTestNotice(author);
        Member missionary= new Member(2L, "간사", "동백2", "동백2", "url", "2025");
        memberRepository.save(missionary);
        saveMemberUniversity(missionary, University.SIRIB);

        // when
        noticeService.deleteNotice(notice.getId(), new MemberAuth(missionary.getId()));

        // then
        assertThat(noticeRepository.existsById(notice.getId())).isFalse();
    }

    private Member saveMember(String role) {
        Member member = new Member(1L, role, "동백", "동백", "url", "2025");
        return memberRepository.save(member);
    }

    private void saveMemberUniversity(Member member, University university) {
        memberUniversityRepository.save(new MemberUniversity(member, university));
    }

    private Notice saveTestNotice(Member author) {
        Notice notice = saveNotice(author);
        List<NoticePhoto> photos = savePhotos(notice);
        List<NoticeUniversity> universities = saveNoticeUniversities(notice);
        return notice;
    }

    private Notice saveNotice(Member author) {
        Notice notice = new Notice("학교", author, "제목", "내용",
                LocalDate.now(), LocalDate.now());
        return noticeRepository.save(notice);
    }

    private List<NoticePhoto> savePhotos(Notice notice) {
        NoticePhoto photo1 = new NoticePhoto(notice, "https://xxx.xxx.xxx");
        NoticePhoto photo2 = new NoticePhoto(notice, "https://yyy.yyy.yyy");
        return noticePhotoRepository.saveAll(List.of(photo1, photo2));
    }

    private List<NoticeUniversity> saveNoticeUniversities(Notice notice) {
        NoticeUniversity university1 = new NoticeUniversity(notice, University.KWANGWOON);
        NoticeUniversity university2 = new NoticeUniversity(notice, University.KONKUK);
        return noticeUniversityRepository.saveAll(List.of(university1, university2));
    }
}
