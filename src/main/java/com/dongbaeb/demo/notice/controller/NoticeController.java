package com.dongbaeb.demo.notice.controller;

import com.dongbaeb.demo.global.dto.MemberAuth;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.notice.domain.NoticeCategory;
import com.dongbaeb.demo.notice.dto.NoticeResponse;
import com.dongbaeb.demo.notice.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Notice 전체 조회 관련 API 요청", description = "Notice 공지 전체 목록 조회 등의 역할을 한다.")
@RequiredArgsConstructor
@RequestMapping("/notice")
@RestController
public class NoticeController {

    private final NotificationService notificationService;

//    @GetMapping("/councils/{page}")
//    public ResponseEntity<List<Map<String, Object>>> getAllCouncilsNotification(@PathVariable("page") Integer page) {
//        ResponseEntity<List<Map<String, Object>>> response = notificationService.getAllCouncilsNotification(page);
//        return response;
//    }
//
//    @GetMapping("/university/{name}")
//    public ResponseEntity<List<Map<String, Object>>> getByUniversityName(@PathVariable("name") University name,
//                                                                         MemberAuth memberAuth) {
//        ResponseEntity<List<Map<String, Object>>> response = notificationService.getByUniversityName(name, memberAuth);
//        return response;
//    }

    @Operation(summary = "조건에 맞는 공지를 가져온다.", description = "category와 name에 맞는 공지를 가져온다. page, size를 통해 가져오는 양을 조절할 수 있다.")
    @GetMapping("/")
    public ResponseEntity<List<NoticeResponse>> getNotices(
            @Parameter(description = "공지 카테고리 정보")
            @RequestParam("category") NoticeCategory noticeCategory,
            @Parameter(description = "조회 하고 싶은 학교 이름")
            @RequestParam("name") University name,
            Pageable pageable,
            MemberAuth memberAuth) {

        List<NoticeResponse> notices;

        if (NoticeCategory.EAST_SEOUL.equals(noticeCategory)) {
            notices = notificationService.getAllCouncilsNotification(pageable);
        } else {
            notices = notificationService.getByUniversityName(name, pageable, memberAuth);
        }

        return ResponseEntity.ok(notices);
    }
}
