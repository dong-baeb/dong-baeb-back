package com.dongbaeb.demo.notification.service;

import com.dongbaeb.demo.global.dto.MemberAuth;
import com.dongbaeb.demo.global.exception.BadRequestException;
import com.dongbaeb.demo.global.exception.ForbiddenException;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.member.repository.MemberRepository;
import com.dongbaeb.demo.member.repository.MemberUniversityRepository;
import com.dongbaeb.demo.notification.domain.Notification;
import com.dongbaeb.demo.notification.domain.NotificationPhoto;
import com.dongbaeb.demo.notification.domain.NotificationUniversity;
import com.dongbaeb.demo.notification.dto.NotificationRequest;
import com.dongbaeb.demo.notification.repository.NotificationPhotoRepository;
import com.dongbaeb.demo.notification.repository.NotificationRepository;
import com.dongbaeb.demo.notification.repository.NotificationUniversityRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationPhotoRepository notificationPhotoRepository;
    private final NotificationUniversityRepository notificationUniversityRepository;
    private final MemberRepository memberRepository;
    private final MemberUniversityRepository memberUniversityRepository;

    @Transactional
    public Long createNotification(NotificationRequest request, MemberAuth memberAuth) {
        Member author = findMemberById(memberAuth.memberId());
        Notification notification = request.toNotification(author);
        List<University> universities = getUniversities(request);
        createNotification(notification, author, universities);
        createNotificationPhotos(notification, request);
        createNotificationUniversities(universities, notification);

        return notification.getId();
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 멤버의 id입니다."));
    }

    private List<University> getUniversities(NotificationRequest request) {
        return request.universities()
                .stream()
                .map(University::fromName)
                .toList();
    }

    private void validateUniversityCount(Notification notification, List<University> universities) {
        if (!notification.isValidUniversityCount(universities.size())) {
            throw new BadRequestException("공지의 학교 개수가 올바르지 않습니다.");
        }
    }

    private void createNotification(Notification notification, Member author, List<University> universities) {
        validateStartDate(notification);
        validateUniversityCount(notification, universities);
        validateAuthorization(notification, author, universities);
        notificationRepository.save(notification);
    }

    private void validateStartDate(Notification notification) {
        if (notification.isStartDateBefore(LocalDate.now())) {
            throw new BadRequestException("시작 날짜는 오늘보다 이전일 수 없습니다.");
        }
    }

    private void validateAuthorization(Notification notification, Member author, List<University> universities) {
        if (!hasPermissionToCreate(notification, author, universities)) {
            throw new ForbiddenException("공지를 작성할 권한이 없습니다.");
        }
    }

    private boolean hasPermissionToCreate(Notification notification, Member author, List<University> universities) {
        return notification.isRoleAllowed() || isLeaderOfAnyUniversity(author, universities);
    }

    private boolean isLeaderOfAnyUniversity(Member author, List<University> universities) {
        return author.isLeader() && memberUniversityRepository.existsAllByMemberAndUniversityIn(author, universities);
    }

    private void createNotificationPhotos(Notification notification, NotificationRequest request) {
        List<NotificationPhoto> notificationPhotos = request.imageUrls()
                .stream()
                .map(imageUrl -> new NotificationPhoto(notification, imageUrl))
                .toList();
        notificationPhotoRepository.saveAll(notificationPhotos);
    }

    private void createNotificationUniversities(List<University> universities, Notification notification) {
        List<NotificationUniversity> notificationUniversities = universities
                .stream()
                .map(university -> new NotificationUniversity(notification, university))
                .toList();
        notificationUniversityRepository.saveAll(notificationUniversities);
    }
}
