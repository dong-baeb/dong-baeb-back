package com.dongbaeb.demo.member.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.dongbaeb.demo.global.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UniversityTest {
    @DisplayName("올바른 학교 이름이 주어진 경우 예외가 발생하지 않는다.")
    @ParameterizedTest
    @ValueSource(strings = {"건국대학교", "장로회신학대학교", "광운대학교", "서울시립대학교", "동덕여자대학교", "세종대학교", "서울과학기술대학교", "서울여자대학교",
            "아세아연합신학대학교", "삼육대학교", "신구대학교", "가천대학교 글로벌캠퍼스", "을지대학교", "서울장신대학교", "한국외국어대학교", "한양대학교", "한양여자대학교"
    })
    void fromNameSuccessTest(String name) {
        assertThatCode(() -> University.fromName(name))
                .doesNotThrowAnyException();
    }

    @DisplayName("올바르지 않은 학교 이름이 주어진 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "낙성대학교"})
    void fromNameNotExistsExceptionTest(String name) {
        assertThatCode(() -> University.fromName(name))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("동서울 IVF에 존재하지 않는 대학교 이름입니다.");
    }
}
