package com.dongbaeb.demo.notice.controller;

import com.dongbaeb.demo.auth.infrastructure.JwtTokenProvider;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.member.repository.MemberRepository;
import com.dongbaeb.demo.notice.domain.Notice;
import com.dongbaeb.demo.notice.domain.NoticePhoto;
import com.dongbaeb.demo.notice.domain.NoticeUniversity;
import com.dongbaeb.demo.notice.dto.NoticeRequest;
import com.dongbaeb.demo.notice.dto.NoticeResponse;
import com.dongbaeb.demo.notice.repository.NoticePhotoRepository;
import com.dongbaeb.demo.notice.repository.NoticeRepository;
import com.dongbaeb.demo.notice.repository.NoticeUniversityRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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

import java.time.LocalDate;
import java.util.List;

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
    @DisplayName("공지를 작성한다.")
    void createNoticeTest() {
        Member member = saveMember();
        NoticeRequest noticeRequest =
                new NoticeRequest("동서울", "제목", "내용", LocalDate.now(), LocalDate.now(), List.of("url"), List.of());

        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + createToken(member))
                .contentType(ContentType.JSON)
                .body(noticeRequest)
                .when().post("/notices")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/notices/2");
    }

    @Test
    @DisplayName("공지를 정상적으로 조회한다.")
    void readNoticeTest() {
        // given
        Member author = saveMember();
        Notice notice = saveNotice(author);
        List<NoticePhoto> photos = savePhotos(notice);
        List<NoticeUniversity> noticeUniversities = saveNoticeUniversities(notice);
        NoticeResponse expectedResponse = NoticeResponse.from(notice, photos, noticeUniversities);

        // when
        NoticeResponse actualResponse = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + createToken(author))
                .when().get("/notices/" + notice.getId())
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(NoticeResponse.class);

        System.out.println(actualResponse.toString());
        System.out.println(expectedResponse.toString());

        //then
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("공지를 정상적으로 삭제한다.")
    void deleteNoticeTest() {
        // given
        Member author = saveMember();
        Notice notice = saveNotice(author);
        savePhotos(notice);
        saveNoticeUniversities(notice);

        // when
        RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + createToken(author))
                .when().delete("/notices/" + notice.getId())
                .then().log().all()
                .statusCode(204);

        // then
        assertThat(noticeRepository.existsById(notice.getId())).isFalse();
        assertThat(noticePhotoRepository.findByNoticeId(notice.getId())).isEmpty();
        assertThat(noticeUniversityRepository.findByNoticeId(notice.getId())).isEmpty();
    }

    private Member saveMember() {
        Member member = new Member(1L, "간사", "동백", "동백", "https://xxx.xxx.xxx", "2025");
        return memberRepository.save(member);
    }

    private Notice saveNotice(Member author) {
        Notice notice = new Notice("동서울", author, "동서울 연합 수련회", "동서울 연합 수련회를 진행합니다!",
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

    private String createToken(Member member) {
        return jwtTokenProvider.createAccessToken(member.getId());
    }
}
