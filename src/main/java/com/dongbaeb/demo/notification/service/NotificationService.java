package com.dongbaeb.demo.notification.service;

import com.dongbaeb.demo.global.dto.MemberAuth;
import com.dongbaeb.demo.global.exception.BadRequestException;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.repository.MemberRepository;
import com.dongbaeb.demo.member.repository.MemberUniversityRepository;
import com.dongbaeb.demo.notification.domain.Notification;
import com.dongbaeb.demo.notification.domain.NotificationPhoto;
import com.dongbaeb.demo.notification.domain.NotificationUniversity;
import com.dongbaeb.demo.notification.dto.NotificationRequest;
import com.dongbaeb.demo.notification.repository.NotificationPhotoRepository;
import com.dongbaeb.demo.notification.repository.NotificationRepository;
import com.dongbaeb.demo.notification.repository.NotificationUniversityRepository;
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
        notificationRepository.save(notification);
        createNotificationPhotos(notification, request);
        createNotificationUniversities(request, notification);

        return notification.getId();
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 멤버의 id입니다."));
    }

    private void createNotificationPhotos(Notification notification, NotificationRequest request) {
        List<NotificationPhoto> notificationPhotos = request.imageUrls()
                .stream()
                .map(imageUrl -> new NotificationPhoto(notification, imageUrl))
                .toList();
        notificationPhotoRepository.saveAll(notificationPhotos);
    }

    private void createNotificationUniversities(NotificationRequest request, Notification notification) {
        List<NotificationUniversity> notificationUniversities = request.universities()
                .stream()
                .map(university -> new NotificationUniversity(notification, university))
                .toList();
        notificationUniversityRepository.saveAll(notificationUniversities);
    }
}
