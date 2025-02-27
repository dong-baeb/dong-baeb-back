package com.dongbaeb.demo.notice.controller;

import com.dongbaeb.demo.auth.infrastructure.JwtTokenProvider;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.member.repository.MemberRepository;
import com.dongbaeb.demo.notice.domain.Notice;
import com.dongbaeb.demo.notice.domain.NoticePhoto;
import com.dongbaeb.demo.notice.domain.NoticeUniversity;
import com.dongbaeb.demo.notice.dto.NoticeResponse;
import com.dongbaeb.demo.notice.repository.NoticePhotoRepository;
import com.dongbaeb.demo.notice.repository.NoticeRepository;
import com.dongbaeb.demo.notice.repository.NoticeUniversityRepository;
import io.restassured.RestAssured;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(properties = {"spring.config.location = classpath:test-application.yml"})
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class NoticeControllerTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    NoticePhotoRepository noticePhotoRepository;
    @Autowired
    NoticeUniversityRepository noticeUniversityRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("공지를 정상적으로 조회한다.")
    void readNoticeTest() {
        // given
        Member author = saveMember();
        Notice notice = saveNotice(author);
        List<NoticePhoto> photos = savePhotos(notice);
        List<NoticeUniversity> universities = saveUniversities(notice);
        NoticeResponse expectedResponse = NoticeResponse.from(notice, photos, universities);

        // when
        NoticeResponse actualResponse = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + createToken(author))
                .when().get("/notices/" + notice.getId())
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(NoticeResponse.class);

        //then
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    private Member saveMember() {
        Member member = new Member(1L, "간사", "동백", "동백", "https://xxx.xxx.xxx", "2025");
        return memberRepository.save(member);
    }

    private Notice saveNotice(Member author) {
        Notice notice = new Notice("동서울", author, "동서울 연합 수련회", "동서울 연합 수련회를 진행합니다!",
                LocalDate.parse("2025-12-31"), LocalDate.parse("2025-12-31"));
        return noticeRepository.save(notice);
    }

    private List<NoticePhoto> savePhotos(Notice notice) {
        NoticePhoto photo1 = new NoticePhoto(notice, "https://xxx.xxx.xxx");
        NoticePhoto photo2 = new NoticePhoto(notice, "https://yyy.yyy.yyy");
        return noticePhotoRepository.saveAll(List.of(photo1, photo2));
    }

    private List<NoticeUniversity> saveUniversities(Notice notice) {
        NoticeUniversity university1 = new NoticeUniversity(notice, University.KWANGWOON);
        NoticeUniversity university2 = new NoticeUniversity(notice, University.KONKUK);
        return noticeUniversityRepository.saveAll(List.of(university1, university2));
    }

    private String createToken(Member member) {
        return jwtTokenProvider.createAccessToken(member.getId());
    }
}
