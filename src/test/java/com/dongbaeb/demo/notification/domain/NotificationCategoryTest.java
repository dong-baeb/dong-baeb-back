package com.dongbaeb.demo.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.dongbaeb.demo.global.exception.BadRequestException;
import com.dongbaeb.demo.member.domain.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class NotificationCategoryTest {
    @DisplayName("카테고리를 찾을 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"동서울", "학교"})
    void fromTest(String category) {
        assertThatCode(() -> NotificationCategory.from(category))
                .doesNotThrowAnyException();
    }

    @DisplayName("카테고리를 찾을 때 존재하지 않는 카테고리라면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"서서울", "전체"})
    void fromWithNotExistsExceptionTest(String category) {
        assertThatCode(() -> NotificationCategory.from(category))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("존재하지 않는 공지 카테고리입니다.");
    }

    @DisplayName("카테고리를 찾을 때 null이거나 비어있다면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void fromWithNullAndEmptyExceptionTest(String category) {
        assertThatCode(() -> NotificationCategory.from(category))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("존재하지 않는 공지 카테고리입니다.");
    }

    @DisplayName("동서울 카테고리 공지는 간사만 작성하는 것이 허용된다.")
    @Test
    void isAllowedOnEastSeoulWithMissionaryTest() {
        assertThat(NotificationCategory.EAST_SEOUL.isRoleAllowed(Role.MISSIONARY))
                .isTrue();
    }

    @DisplayName("동서울 카테고리 공지는 리더, 멤버, 학사가 작성하는 것을 허용하지 않는다.")
    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"MEMBER", "LEADER", "GRADUATE"})
    void isAllowedOnEastSeoulWithMemberAndLeaderAndGraduateTest(Role role) {
        assertThat(NotificationCategory.EAST_SEOUL.isRoleAllowed(role))
                .isFalse();
    }

    @DisplayName("학교 카테고리 공지는 간사와 리더만 작성하는 것이 허용된다.")
    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"MISSIONARY", "LEADER"})
    void isAllowedOnUniversityWithMissionaryTest(Role role) {
        assertThat(NotificationCategory.UNIVERSITY.isRoleAllowed(role))
                .isTrue();
    }

    @DisplayName("학교 카테고리 공지는 멤버, 학사가 작성하는 것을 허용하지 않는다.")
    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"MEMBER", "GRADUATE"})
    void isAllowedOnUniversityWithMemberAndLeaderAndGraduateTest(Role role) {
        assertThat(NotificationCategory.UNIVERSITY.isRoleAllowed(role))
                .isFalse();
    }

    @DisplayName("동서울 공지는 0개의 학교에 소속될 수 있다.")
    @Test
    void isValidUniversityCountOnEastSeoulWithZeroTest() {
        assertThat(NotificationCategory.EAST_SEOUL.isValidUniversityCount(0)).isTrue();
    }

    @DisplayName("동서울 공지는 1개 이상의 학교에 소속될 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    void isValidUniversityCountOnEastSeoulWithMoreThanOneTest(int universityCount) {
        assertThat(NotificationCategory.EAST_SEOUL.isValidUniversityCount(universityCount)).isFalse();
    }

    @DisplayName("학교 공지는 1개 이상의 학교에 소속될 수 있다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    void isValidUniversityCountOnUniversityWithMoreThanOneTest(int universityCount) {
        assertThat(NotificationCategory.UNIVERSITY.isValidUniversityCount(universityCount)).isTrue();
    }

    @DisplayName("학교 공지는 0개의 학교에 소속될 수 없다.")
    @Test
    void isValidUniversityCountOnUniversityWithZeroTest() {
        assertThat(NotificationCategory.UNIVERSITY.isValidUniversityCount(0)).isFalse();
    }
}
