package com.dongbaeb.demo.notification.service;

import com.dongbaeb.demo.global.dto.MemberAuth;
import com.dongbaeb.demo.global.exception.ForbiddenException;
import com.dongbaeb.demo.global.exception.ResourceNotFoundException;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.MemberUniversity;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.member.repository.MemberRepository;
import com.dongbaeb.demo.member.repository.MemberUniversityRepository;
import com.dongbaeb.demo.notification.domain.Notification;
import com.dongbaeb.demo.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final MemberRepository memberRepository;

    private final MemberUniversityRepository memberUniversityRepository;

    public ResponseEntity<?> getAllCouncilsNotification(int page) {
        int pageSize = 15;
        int offset = (page - 1) * pageSize;
        ArrayList<Notification> notifications = notificationRepository.findPagedWholeEntities(pageSize,offset);
        return new ResponseEntity<>(notifications, HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> getByUniversityName(University name,MemberAuth memberAuth) {
        validateUniversity(memberAuth.memberId(),name);
        ArrayList<Notification> notifications = notificationRepository.findByUniversity(name);
        return new ResponseEntity<>(notifications,HttpStatusCode.valueOf(200));
    }

    private void validateUniversity(Long memberId, University name) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 아이디를 가진 사용자를 찾을 수 없습니다: " + memberId));
        ArrayList<MemberUniversity> memberUniversities = memberUniversityRepository.findByMember(member);
        if(!isExistUniversity(memberUniversities,name)) {
            throw new ForbiddenException("다른 대학교의 공지에 접근할 수 없습니다.");
        }
    }

    private boolean isExistUniversity(ArrayList<MemberUniversity> memberUniversities, University name) {
        boolean isExist = false;
        for(int i=0;i<memberUniversities.size();i++) {
            if(Objects.equals(memberUniversities.get(i).getUniversity(),name)) {
                isExist=true;
                break;
            }
        }
        return isExist;
    }
}
