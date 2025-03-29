package com.dongbaeb.demo.notification.service;

import com.dongbaeb.demo.global.dto.MemberAuth;
import com.dongbaeb.demo.global.exception.ForbiddenException;
import com.dongbaeb.demo.global.exception.ResourceNotFoundException;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.MemberUniversity;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.member.repository.MemberRepository;
import com.dongbaeb.demo.member.repository.MemberUniversityRepository;
import com.dongbaeb.demo.notification.domain.Notice;
import com.dongbaeb.demo.notification.domain.NoticePhoto;
import com.dongbaeb.demo.notification.domain.NoticeUniversity;
import com.dongbaeb.demo.notification.repository.NotificationPhotoRepository;
import com.dongbaeb.demo.notification.repository.NotificationRepository;
import com.dongbaeb.demo.notification.repository.NotificationUniversityRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    private final MemberUniversityRepository memberUniversityRepository;
    private final NotificationPhotoRepository notificationPhotoRepository;

    private final NotificationUniversityRepository notificationUniversityRepository;

    public ResponseEntity<List<Map<String, Object>>> getAllCouncilsNotification(int page) {
        ArrayList<Map<String, Object>> datas = new ArrayList<>();

        int pageSize = 15;
        int offset = (page - 1) * pageSize;
        List<Notice> notices = notificationRepository.findPagedWholeEntities(pageSize, offset);

        for (int i = 0; i < notices.size(); i++) {
            Map<String, Object> data = new HashMap<>();
            Notice thisNotice = notices.get(i);
            data.put("notification", thisNotice);
            data.put("photos", getPhotoUrls(thisNotice));
            datas.add(data);
        }

        return new ResponseEntity<>(datas, HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<List<Map<String, Object>>> getByUniversityName(University name, MemberAuth memberAuth) {

//        validateUniversity(memberAuth.memberId(),name);

        List<Map<String, Object>> datas = new ArrayList<>();

        List<NoticeUniversity> notifications = notificationUniversityRepository.findByUniversity(name);

        for (int i = 0; i < notifications.size(); i++) {
            Map<String, Object> data = new HashMap<>();
            Notice thisNotice = notifications.get(i).getNotice();
            data.put("notification", thisNotice);
            data.put("photos", getPhotoUrls(thisNotice));
            datas.add(data);
        }

        return new ResponseEntity<>(datas, HttpStatusCode.valueOf(200));
    }

    private List<String> getPhotoUrls(Notice notice) {
        return notificationPhotoRepository.findByNotice(notice)
                .stream()
                .map(NoticePhoto::getImageUrl)
                .toList();
    }

    private void validateUniversity(Long memberId, University name) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 아이디를 가진 사용자를 찾을 수 없습니다: " + memberId));
        List<MemberUniversity> memberUniversities = memberUniversityRepository.findByMember(member);
        if (!isExistUniversity(memberUniversities, name)) {
            throw new ForbiddenException("다른 대학교의 공지에 접근할 수 없습니다.");
        }
    }

    private boolean isExistUniversity(List<MemberUniversity> memberUniversities, University name) {
        boolean isExist = false;
        for (int i = 0; i < memberUniversities.size(); i++) {
            if (Objects.equals(memberUniversities.get(i).getUniversity(), name)) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }
}
