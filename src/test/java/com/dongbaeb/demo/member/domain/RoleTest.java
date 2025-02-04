package com.dongbaeb.demo.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dongbaeb.demo.global.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

class RoleTest {
    @DisplayName("학교 수가 음수인 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -100})
    void isValidUniversityCountNegativeCountExceptionTest(int universitiesCount) {
        assertThatCode(() -> Role.MEMBER.isValidUniversityCount(universitiesCount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("학교 수는 양수이어야 합니다.");
    }

    @DisplayName("멤버, 리더, 학사는 1개의 학교에만 소속될 수 있다.")
    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"MEMBER", "LEADER", "BACHELOR"})
    void isValidUniversityCountWithStudentTrueTest(Role role) {
        assertThat(role.isValidUniversityCount(1))
                .isTrue();
    }

    @DisplayName("멤버, 리더, 학사는 0개나 2개 이상의 학교에 소속될 수 없다.")
    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"MEMBER", "LEADER", "BACHELOR"})
    void isValidUniversityCountWithStudentFalseTest(Role role) {
        assertAll(
                () -> assertThat(role.isValidUniversityCount(0)).isFalse(),
                () -> assertThat(role.isValidUniversityCount(2)).isFalse()
        );
    }

    @DisplayName("간사는 학교에 아예 소속되지 않거나 여러 개의 학교에 소속될 수 있다.")
    @Test
    void isValidUniversityCountWithMissionaryTrueTest() {
        assertAll(
                () -> assertThat(Role.MISSIONARY.isValidUniversityCount(0)).isTrue(),
                () -> assertThat(Role.MISSIONARY.isValidUniversityCount(1)).isTrue(),
                () -> assertThat(Role.MISSIONARY.isValidUniversityCount(2)).isTrue()
        );
    }

    @DisplayName("올바른 역할 이름이 주어진 경우 예외가 발생하지 않는다.")
    @ParameterizedTest
    @ValueSource(strings = {"멤버", "리더", "간사", "학사"})
    void fromSuccessTest(String role) {
        assertThatCode(() -> Role.from(role))
                .doesNotThrowAnyException();
    }

    @DisplayName("올바르지 않은 역할 이름이 주어진 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "간사급 리더"})
    void fromNotExistsExceptionTest(String role) {
        assertThatCode(() -> Role.from(role))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("해당 " + role + "에 해당되는 역할이 존재하지 않습니다.");
    }
}
