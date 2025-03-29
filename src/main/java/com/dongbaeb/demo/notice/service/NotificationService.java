package com.dongbaeb.demo.notice.service;

import com.dongbaeb.demo.global.dto.MemberAuth;
import com.dongbaeb.demo.global.exception.ForbiddenException;
import com.dongbaeb.demo.global.exception.ResourceNotFoundException;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.MemberUniversity;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.member.repository.MemberRepository;
import com.dongbaeb.demo.member.repository.MemberUniversityRepository;
import com.dongbaeb.demo.notice.domain.Notice;
import com.dongbaeb.demo.notice.domain.NoticePhoto;
import com.dongbaeb.demo.notice.domain.NoticeUniversity;
import com.dongbaeb.demo.notice.dto.NoticeResponse;
import com.dongbaeb.demo.notice.repository.NotificationPhotoRepository;
import com.dongbaeb.demo.notice.repository.NotificationRepository;
import com.dongbaeb.demo.notice.repository.NotificationUniversityRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public List<NoticeResponse> getAllCouncilsNotification(int page) {
        int pageSize = 15;
        int offset = (page - 1) * pageSize;

        List<NoticeResponse> noticeResponses = new ArrayList<>();
        List<Notice> notices = notificationRepository.findPagedWholeEntities(pageSize, offset);

        for (int i = 0; i < notices.size(); i++) {
            Notice notice = notices.get(i);
            List<NoticePhoto> noticePhotos = notificationPhotoRepository.findByNotice(notice);
            noticeResponses.add(NoticeResponse.from(notice, noticePhotos, new ArrayList<>()));
        }

        return noticeResponses;
    }

    public List<NoticeResponse> getByUniversityName(University name, MemberAuth memberAuth) {

//        validateUniversity(memberAuth.memberId(),name);

        List<NoticeResponse> noticeResponses = new ArrayList<>();

        List<NoticeUniversity> noticeUniversities = notificationUniversityRepository.findByUniversity(name);

        for (int i = 0; i < noticeUniversities.size(); i++) {
            Notice notice = noticeUniversities.get(i).getNotice();
            List<NoticePhoto> noticePhotos = notificationPhotoRepository.findByNotice(notice);
            List<NoticeUniversity> universities = notificationUniversityRepository.findByNotice(notice);
            noticeResponses.add(NoticeResponse.from(notice, noticePhotos, universities));
        }

        return noticeResponses;
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
