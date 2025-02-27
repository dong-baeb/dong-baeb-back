package com.dongbaeb.demo.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dongbaeb.demo.global.dto.MemberAuth;
import com.dongbaeb.demo.global.exception.BadRequestException;
import com.dongbaeb.demo.global.exception.ForbiddenException;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.MemberUniversity;
import com.dongbaeb.demo.member.domain.Role;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.member.repository.MemberRepository;
import com.dongbaeb.demo.member.repository.MemberUniversityRepository;
import com.dongbaeb.demo.notification.dto.NotificationRequest;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@Import({NotificationService.class})
@TestPropertySource(properties = {"spring.config.location = classpath:test-application.yml"})
class NotificationServiceTest {
    @Autowired
    NotificationService notificationService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberUniversityRepository memberUniversityRepository;

    @Test
    @DisplayName("과거 날짜에 대한 공지 작성 시 예외가 발생한다.")
    void createNotificationWithPastStartDayExceptionTest() {
        // given
        Member member = saveMember(Role.MISSIONARY);
        LocalDate pastDate = LocalDate.now().minusDays(1L);
        NotificationRequest request =
                new NotificationRequest("동서울", "제목", "내용", pastDate, LocalDate.now(), List.of("url"), List.of());

        // when & then
        assertThatThrownBy(() -> notificationService.createNotification(request, new MemberAuth(member.getId())))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("과거 날짜에 대한 공지는 작성할 수 없습니다.");
    }

    @Test
    @DisplayName("공지의 학교 개수가 올바르지 않은 경우 예외가 발생한다.")
    void createNotificationWithUnValidUniversityCountExceptionTest() {
        // given
        Member member = saveMember(Role.MISSIONARY);
        List<String> unValidUniversities = List.of();
        NotificationRequest request = new NotificationRequest(
                "학교", "제목", "내용", LocalDate.now(), LocalDate.now(), List.of("url"), unValidUniversities);

        // when & then
        assertThatThrownBy(() -> notificationService.createNotification(request, new MemberAuth(member.getId())))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("공지의 학교 개수가 올바르지 않습니다.");
    }

    @Test
    @DisplayName("올바르지 않은 역할의 멤버가 공지를 작성하는 경우 예외가 발생한다.")
    void createNotificationWithUnValidRoleExceptionTest() {
        // given
        MemberAuth unValidRoleMemberAuth = new MemberAuth(saveMember(Role.MEMBER).getId());
        NotificationRequest request =
                new NotificationRequest("동서울", "제목", "내용", LocalDate.now(), LocalDate.now(), List.of("url"), List.of());

        // when & then
        assertThatThrownBy(() -> notificationService.createNotification(request, unValidRoleMemberAuth))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("공지를 작성할 권한이 없습니다.");
    }

    @Test
    @DisplayName("리더가 자신이 소속되지 않은 학교의 공지를 작성하는 경우 예외가 발생한다.")
    void createNotificationWithLeaderNotBelongToUniversityExceptionTest() {
        // given
        Member leader = saveMember(Role.LEADER);
        University leaderUniversity = University.KONKUK;
        saveMemberUniversity(leader, leaderUniversity);
        List<String> universitiesLeaderNotBelongTo = List.of("한양대학교", "건국대학교");
        NotificationRequest request = new NotificationRequest(
                "학교", "제목", "내용", LocalDate.now(), LocalDate.now(), List.of("url"), universitiesLeaderNotBelongTo);

        // when & then
        assertThatThrownBy(() -> notificationService.createNotification(request, new MemberAuth(leader.getId())))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("공지를 작성할 권한이 없습니다.");
    }

    // TODO: 테스트 격리 이후 id 값 검증하기
    @Test
    @DisplayName("리더는 자신이 소속된 학교의 공지만 작성할 수 있다.")
    void createNotificationWithLeaderBelongToUniversityTest() {
        // given
        Member leader = saveMember(Role.LEADER);
        University leaderUniversity = University.KONKUK;
        saveMemberUniversity(leader, leaderUniversity);
        List<String> universitiesLeaderBelongTo = List.of(leaderUniversity.getName());
        NotificationRequest request = new NotificationRequest(
                "학교", "제목", "내용", LocalDate.now(), LocalDate.now(), List.of("url"), universitiesLeaderBelongTo);

        // when
        Long notificationId = notificationService.createNotification(request, new MemberAuth(leader.getId()));

        // then
        assertThat(notificationId).isNotNull();
    }

    // TODO: 테스트 격리 이후 id 값 검증하기
    @Test
    @DisplayName("간사는 자신이 소속되지 않은 학교의 공지를 작성할 수 있다.")
    void createNotificationWithMissionaryNotBelongToUniversityTest() {
        // given
        Member missionary = saveMember(Role.MISSIONARY);
        University missionaryUniversity = University.KONKUK;
        saveMemberUniversity(missionary, missionaryUniversity);
        List<String> universitiesNotLeaderBelongTo = List.of("한양대학교");
        NotificationRequest request = new NotificationRequest(
                "학교", "제목", "내용", LocalDate.now(), LocalDate.now(), List.of("url"), universitiesNotLeaderBelongTo);

        // when
        Long notificationId = notificationService.createNotification(request, new MemberAuth(missionary.getId()));

        // then
        assertThat(notificationId).isNotNull();
    }

    private Member saveMember(Role role) {
        Member member = new Member(1L, role, "동백", "동백", "url", "2025");
        return memberRepository.save(member);
    }

    private MemberUniversity saveMemberUniversity(Member member, University university) {
        return memberUniversityRepository.save(new MemberUniversity(member, university));
    }
}
