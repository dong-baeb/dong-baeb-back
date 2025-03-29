package com.dongbaeb.demo.notification.controller;

import com.dongbaeb.demo.global.dto.MemberAuth;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.notification.domain.NoticeCategory;
import com.dongbaeb.demo.notification.service.NotificationService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/notifications")
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

    @GetMapping("/")
    public ResponseEntity<List<Map<String, Object>>> getNotices(@RequestParam("category") NoticeCategory noticeCategory,
                                                                @RequestParam("name") University name,
                                                                @RequestParam("page") Integer page,
                                                                MemberAuth memberAuth) {
        ResponseEntity<List<Map<String, Object>>> response;

        if (NoticeCategory.EAST_SEOUL.equals(noticeCategory)) {
            response = notificationService.getAllCouncilsNotification(page);
        } else {
            response = notificationService.getByUniversityName(name, memberAuth);
        }

        return response;
    }
}
