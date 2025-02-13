package com.dongbaeb.demo.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.dongbaeb.demo.global.exception.BadRequestException;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.Role;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class NotificationTest {
    @Test
    @DisplayName("공지의 끝 날짜는 시작 날짜보다 앞설 수 없다.")
    void createWithEndDateFasterThanStartDateExceptionTest() {
        // given
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = start.minusDays(1);
        Member member = createMember(Role.MISSIONARY);

        // when & then
        assertThatCode(() -> createNotification(NotificationCategory.EAST_SEOUL, member, start, end))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("공지의 끝 날짜는 시작 날짜를 앞설 수 없습니다.");
    }

    @Test
    @DisplayName("공지를 생성할 수 있다.")
    void createTest() {
        // given
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now();
        Member member = createMember(Role.MISSIONARY);

        // when & then
        assertThatCode(() -> createNotification(NotificationCategory.EAST_SEOUL, member, start, end))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("provideFutureDays")
    @DisplayName("미래 날짜에 대한 공지는 작성이 가능하다.")
    void isNotificationFutureWithPastTest(LocalDate start) {
        // given
        LocalDate end = start.plusDays(1);
        Member member = createMember(Role.MISSIONARY);
        Notification notification = createNotification(NotificationCategory.EAST_SEOUL, member, start, end);

        // when & then
        assertThat(notification.isNotificationPast()).isFalse();
    }

    private static List<LocalDate> provideFutureDays() {
        return List.of(
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );
    }

    @Test
    @DisplayName("과거 날짜에 대한 공지는 작성이 불가능하다.")
    void isNotificationPastWithPastTest() {
        // given
        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = start.plusDays(1);
        Member member = createMember(Role.MISSIONARY);
        Notification notification = createNotification(NotificationCategory.EAST_SEOUL, member, start, end);

        // when & then
        assertThat(notification.isNotificationPast()).isTrue();
    }

    @Test
    @DisplayName("간사는 동서울 공지를 작성할 수 있다.")
    void isRoleAllowedEastSeoulNotificationWithMissionaryTest() {
        // given
        Member member = createMember(Role.MISSIONARY);
        Notification notification =
                createNotification(NotificationCategory.EAST_SEOUL, member, LocalDate.now(), LocalDate.now());

        // when & then
        assertThat(notification.isRoleAllowed()).isTrue();
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"LEADER", "MEMBER", "GRADUATE"})
    @DisplayName("리더, 멤버, 학사는 동서울 공지를 작성할 수 없다.")
    void isRoleAllowedEastSeoulNotificationWithLeaderAndMemberAndGraduateTest(Role role) {
        // given
        Member member = createMember(role);
        Notification notification =
                createNotification(NotificationCategory.EAST_SEOUL, member, LocalDate.now(), LocalDate.now());

        // when & then
        assertThat(notification.isRoleAllowed()).isFalse();
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"MISSIONARY", "LEADER"})
    @DisplayName("간사, 리더는 학교 공지를 작성할 수 있다.")
    void isRoleAllowedUniversityNotificationWithMissionaryAndLeaderTest(Role role) {
        // given
        Member member = createMember(role);
        Notification notification =
                createNotification(NotificationCategory.UNIVERSITY, member, LocalDate.now(), LocalDate.now());

        // when & then
        assertThat(notification.isRoleAllowed()).isTrue();
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"MEMBER", "GRADUATE"})
    @DisplayName("멤버, 학사는 학교 공지를 작성할 수 있다.")
    void isRoleAllowedUniversityNotificationWithMemberAndGraduateTest(Role role) {
        // given
        Member member = createMember(role);
        Notification notification =
                createNotification(NotificationCategory.UNIVERSITY, member, LocalDate.now(), LocalDate.now());

        // when & then
        assertThat(notification.isRoleAllowed()).isFalse();
    }

    @Test
    @DisplayName("동서울 공지는 0개의 학교에 소속될 수 있다.")
    void isValidUniversityCountOnEastSeoulWithZeroTest() {
        // given
        Member member = createMember(Role.MISSIONARY);
        Notification notification =
                createNotification(NotificationCategory.EAST_SEOUL, member, LocalDate.now(), LocalDate.now());

        // when & then
        assertThat(notification.isValidUniversityCount(0)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    @DisplayName("동서울 공지는 1개 이상의 학교에 소속될 수 없다.")
    void isValidUniversityCountOnEastSeoulWithMoreThanOneTest(int universityCount) {
        // given
        Member member = createMember(Role.MISSIONARY);
        Notification notification =
                createNotification(NotificationCategory.EAST_SEOUL, member, LocalDate.now(), LocalDate.now());

        // when & then
        assertThat(notification.isValidUniversityCount(universityCount)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    @DisplayName("학교 공지는 1개 이상의 학교에 소속될 수 있다.")
    void isValidUniversityCountOnUniversityWithMoreThanOneTest(int universityCount) {
        // given
        Member member = createMember(Role.MISSIONARY);
        Notification notification =
                createNotification(NotificationCategory.UNIVERSITY, member, LocalDate.now(), LocalDate.now());

        // when & then
        assertThat(notification.isValidUniversityCount(universityCount)).isTrue();
    }

    @Test
    @DisplayName("학교 공지는 0개의 학교에 소속될 수 없다.")
    void isValidUniversityCountOnUniversityWithZeroTest() {
        // given
        Member member = createMember(Role.MISSIONARY);
        Notification notification =
                createNotification(NotificationCategory.UNIVERSITY, member, LocalDate.now(), LocalDate.now());

        // when & then
        assertThat(notification.isValidUniversityCount(0)).isFalse();
    }

    private static Member createMember(Role role) {
        return new Member(1L, role, "동백이", "동백", null, null);
    }

    private static Notification createNotification(
            NotificationCategory notificationCategory, Member member, LocalDate start, LocalDate end) {
        return new Notification(notificationCategory, member, "제목", "내용", start, end);
    }
}
