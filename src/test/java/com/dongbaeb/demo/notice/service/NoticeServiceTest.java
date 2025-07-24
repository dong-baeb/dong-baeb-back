package com.dongbaeb.demo.notice.service;

import com.dongbaeb.demo.global.dto.MemberAuth;
import com.dongbaeb.demo.global.exception.BadRequestException;
import com.dongbaeb.demo.global.exception.ForbiddenException;
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
import com.dongbaeb.demo.notice.dto.NoticeRequest;
import com.dongbaeb.demo.notice.dto.NoticeResponse;
import com.dongbaeb.demo.notice.repository.NoticePhotoRepository;
import com.dongbaeb.demo.notice.repository.NoticeRepository;
import com.dongbaeb.demo.notice.repository.NoticeUniversityRepository;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import({NoticeService.class})
@TestPropertySource(properties = {"spring.config.location = classpath:test-application.yml"})
class NoticeServiceTest {
    @Autowired
    NoticeService noticeService;
    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberUniversityRepository memberUniversityRepository;
    @Autowired
    NoticePhotoRepository noticePhotoRepository;
    @Autowired
    NoticeUniversityRepository noticeUniversityRepository;


    @Test
    @DisplayName("과거 날짜에 대한 공지 작성 시 예외가 발생한다.")
    void createNoticeWithPastStartDayExceptionTest() {
        // given
        Member member = saveMember(Role.MISSIONARY);
        LocalDate pastDate = LocalDate.now().minusDays(1L);
        NoticeRequest request =
                new NoticeRequest("동서울", "제목", "내용", pastDate, LocalDate.now(), List.of("url"), List.of());

        // when & then
        assertThatThrownBy(() -> noticeService.createNotice(request, new MemberAuth(member.getId())))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("과거 날짜에 대한 공지는 작성할 수 없습니다.");
    }

    @Test
    @DisplayName("공지의 학교 개수가 올바르지 않은 경우 예외가 발생한다.")
    void createNoticeWithUnValidUniversityCountExceptionTest() {
        // given
        Member member = saveMember(Role.MISSIONARY);
        List<String> unValidUniversities = List.of();
        NoticeRequest request = new NoticeRequest(
                "학교", "제목", "내용", LocalDate.now(), LocalDate.now(), List.of("url"), unValidUniversities);

        // when & then
        assertThatThrownBy(() -> noticeService.createNotice(request, new MemberAuth(member.getId())))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("공지의 학교 개수가 올바르지 않습니다.");
    }

    @Test
    @DisplayName("올바르지 않은 역할의 멤버가 공지를 작성하는 경우 예외가 발생한다.")
    void createNoticeWithUnValidRoleExceptionTest() {
        // given
        MemberAuth unValidRoleMemberAuth = new MemberAuth(saveMember(Role.MEMBER).getId());
        NoticeRequest request =
                new NoticeRequest("동서울", "제목", "내용", LocalDate.now(), LocalDate.now(), List.of("url"), List.of());

        // when & then
        assertThatThrownBy(() -> noticeService.createNotice(request, unValidRoleMemberAuth))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("공지를 작성할 권한이 없습니다.");
    }

    @Test
    @DisplayName("리더가 자신이 소속되지 않은 학교의 공지를 작성하는 경우 예외가 발생한다.")
    void createNoticeWithLeaderNotBelongToUniversityExceptionTest() {
        // given
        Member leader = saveMember(Role.LEADER);
        University leaderUniversity = University.KONKUK;
        saveMemberUniversity(leader, leaderUniversity);
        List<String> universitiesLeaderNotBelongTo = List.of("한양대학교", "건국대학교");
        NoticeRequest request = new NoticeRequest(
                "학교", "제목", "내용", LocalDate.now(), LocalDate.now(), List.of("url"), universitiesLeaderNotBelongTo);

        // when & then
        assertThatThrownBy(() -> noticeService.createNotice(request, new MemberAuth(leader.getId())))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("공지를 작성할 권한이 없습니다.");
    }

    // TODO: 테스트 격리 이후 id 값 검증하기
    @Test
    @DisplayName("리더는 자신이 소속된 학교의 공지만 작성할 수 있다.")
    void createNoticeWithLeaderBelongToUniversityTest() {
        // given
        Member leader = saveMember(Role.LEADER);
        University leaderUniversity = University.KONKUK;
        saveMemberUniversity(leader, leaderUniversity);
        List<String> universitiesLeaderBelongTo = List.of(leaderUniversity.getName());
        NoticeRequest request = new NoticeRequest(
                "학교", "제목", "내용", LocalDate.now(), LocalDate.now(), List.of("url"), universitiesLeaderBelongTo);

        // when
        Long noticeId = noticeService.createNotice(request, new MemberAuth(leader.getId()));

        // then
        assertThat(noticeId).isNotNull();
    }

    // TODO: 테스트 격리 이후 id 값 검증하기
    @Test
    @DisplayName("간사는 자신이 소속되지 않은 학교의 공지를 작성할 수 있다.")
    void createNoticeWithMissionaryNotBelongToUniversityTest() {
        // given
        Member missionary = saveMember(Role.MISSIONARY);
        University missionaryUniversity = University.KONKUK;
        saveMemberUniversity(missionary, missionaryUniversity);
        List<String> universitiesNotLeaderBelongTo = List.of("한양대학교");
        NoticeRequest request = new NoticeRequest(
                "학교", "제목", "내용", LocalDate.now(), LocalDate.now(), List.of("url"), universitiesNotLeaderBelongTo);

        // when
        Long noticeId = noticeService.createNotice(request, new MemberAuth(missionary.getId()));

        // then
        assertThat(noticeId).isNotNull();
    }

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

    @ParameterizedTest
    @ValueSource(strings = {"멤버", "리더", "간사"})
    @DisplayName("누구나 동서울 카테고리 공지를 조회할 수 있다.")
    void readNoticeEastSeoulCategoryTest(String role) {
        // given
        Member author = saveMember("간사");
        Notice notice = new Notice("동서울", author, "제목", "내용", LocalDate.now(), LocalDate.now());
        noticeRepository.save(notice);
        List<String> noticeImageUrls = savePhotos(notice).stream()
                .map(NoticePhoto::getImageUrl)
                .toList();
        Member member = new Member(2L, role, "동백2", "동백2", "url", "2025");
        memberRepository.save(member);

        // when
        NoticeResponse response = noticeService.readNotice(notice.getId(), new MemberAuth(member.getId()));

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(notice.getId());
        assertThat(response.imageUrls()).containsExactlyElementsOf(noticeImageUrls);
    }

    @ParameterizedTest
    @ValueSource(strings = {"멤버", "리더"})
    @DisplayName("다른 학교의 공지 조회 시 예외가 발생한다.")
    void readNoticeValidateUniversityExceptionTest(String role) {
        // given
        Member author = saveMember("리더");
        Notice notice = saveTestNotice(author);
        Member member = new Member(2L, role, "동백2", "동백2", "url", "2025");
        memberRepository.save(member);
        saveMemberUniversity(member, University.SIRIB);

        // when & then
        assertThatThrownBy(() -> noticeService.readNotice(notice.getId(), new MemberAuth(member.getId())))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("공지 조회 권한이 없습니다.");
    }

    @Test
    @DisplayName("다가오는 공지를 조회할 수 있다.")
    void readUpcomingNoticesTest() {
        Member author = saveMember("간사");
        saveMemberUniversity(author, University.SIRIB);
        saveMemberUniversity(author, University.DONGDUK);

        University[] universities = University.values();

        for (int i = 0; i < 30; i++) {
            noticeRepository.save(
                    new Notice(NoticeCategory.EAST_SEOUL, author, "동서울 공지입니다" + String.valueOf(i), "안녕하세요",
                            LocalDate.now().plusDays(i + 1), LocalDate.now().plusDays(i + 12)));
        }
        for (int i = 0; i < 30; i++) {
            Random random = new Random();
            University university = universities[random.nextInt(universities.length)];
            Notice notice = noticeRepository.save(
                    new Notice(NoticeCategory.UNIVERSITY, author, university.getName() + " 공지입니다", "안녕하세요",
                            LocalDate.now().plusDays(i + 1), LocalDate.now().plusDays(i + 12)));
            noticeUniversityRepository.save(new NoticeUniversity(notice, university));
        }
        for (int i = 0; i < 30; i++) {
            Random random = new Random();
            University university = universities[random.nextInt(universities.length)];
            University university2 = universities[random.nextInt(universities.length)];
            Notice notice = noticeRepository.save(
                    new Notice(NoticeCategory.UNIVERSITY, author, university.getName() + " 공지입니다", "안녕하세요",
                            LocalDate.now().plusDays(i + 1), LocalDate.now().plusDays(i + 12)));
            noticeUniversityRepository.save(new NoticeUniversity(notice, university));
            noticeUniversityRepository.save(new NoticeUniversity(notice, university2));
        }

        List<NoticeResponse> noticeResponses = noticeService.readUpcomingNotice(new MemberAuth(author.getId()));

        for (NoticeResponse noticeResponse : noticeResponses) {
            List<String> memberUniversities = noticeResponse.universities();

            if (noticeResponse.category().equals(NoticeCategory.UNIVERSITY.getCategory())) {
                assertThat(memberUniversities.contains(University.SIRIB.getShortName()) || memberUniversities.contains(
                        University.DONGDUK.getShortName())).isTrue();
            } else {
                assertThat(memberUniversities.size()).isEqualTo(0);
            }
            assertThat(noticeResponse.startDate()).isAfter(LocalDate.now());
            assertThat(noticeResponse.startDate()).isBefore(LocalDate.now().plusDays(15));

        }


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
    @DisplayName("멤버 아이디를 이용하여 해당 멤버가 작성한 공지를 조회할 수 있다.")
    void readNoticeByMemberId() {
        Member member = saveMember("멤버");
        Notice notice = saveTestNotice(member);
        Notice notice2 = saveTestNotice(member);
        Notice notice3 = new Notice("동서울", member, "수련회", "수련회가자", LocalDate.now(), LocalDate.now());
        noticeRepository.save(notice3);
        List<NoticePhoto> noticePhotos3 = savePhotos(notice3);

        List<NoticeResponse> foundNotices = noticeService.getNoticeByMemberId(new MemberAuth(member.getId()));

        assertThat(foundNotices.size()).isEqualTo(3);
        for (NoticeResponse foundNotice : foundNotices) {
            assertThat(foundNotice.author()).isEqualTo(member.getName());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"멤버", "리더", "간사"})
    @DisplayName("작성자는 공지를 정상적으로 삭제할 수 있다.")
    void deleteNoticeTest(String role) {
        // given
        Member author = saveMember(role);
        Notice notice = saveTestNotice(author);

        // when
        noticeService.deleteNotice(notice.getId(), new MemberAuth(author.getId()));

        // then
        assertThat(noticeRepository.existsById(notice.getId())).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"멤버", "리더", "간사"})
    @DisplayName("작성자 외의 공지를 삭제하려는 경우 예외가 발생한다.")
    void deleteNoticeExceptionTest(String role) {
        // given
        Member author = saveMember("리더");
        Notice notice = saveTestNotice(author);
        Member member = new Member(2L, role, "동백2", "동백2", "url", "2025");
        memberRepository.save(member);

        // when & then
        assertThatThrownBy(() -> noticeService.deleteNotice(notice.getId(), new MemberAuth(member.getId())))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("공지 삭제 권한이 없습니다.");
    }

    private Member saveMember(Role role) {
        Member member = new Member(1L, role, "동백", "동백", "url", "2025");
        return memberRepository.save(member);
    }

    private Member saveMember(String role) {
        Member member = new Member(1L, role, "동백", "동백", "url", "2025");
        return memberRepository.save(member);
    }


    private MemberUniversity saveMemberUniversity(Member member, University university) {
        return memberUniversityRepository.save(new MemberUniversity(member, university));
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
