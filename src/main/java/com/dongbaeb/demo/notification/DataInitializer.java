package com.dongbaeb.demo.notification;

import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.member.repository.MemberRepository;
import com.dongbaeb.demo.notification.domain.Notification;
import com.dongbaeb.demo.notification.domain.NotificationPhoto;
import com.dongbaeb.demo.notification.domain.NotificationUniversity;
import com.dongbaeb.demo.notification.repository.NotificationPhotoRepository;
import com.dongbaeb.demo.notification.repository.NotificationRepository;
import com.dongbaeb.demo.notification.repository.NotificationUniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;

    private final NotificationPhotoRepository notificationPhotoRepository;

    private final NotificationUniversityRepository notificationUniversityRepository;

    @Override
    public void run(String... args) {
        Member member;
        Notification notification;
        member = memberRepository.save(new Member(32532523L, "멤버", "이세영","쿄쿄","fegweg","ggs"));
        notification = notificationRepository.save(new Notification("학교",member,"안녕","반가워",LocalDate.of(2025,2,10),LocalDate.of(2025,2,15)));
        notificationPhotoRepository.save(new NotificationPhoto(notification,"/123"));
        notificationUniversityRepository.save(new NotificationUniversity(notification,University.SIRIB));

        memberRepository.save(new Member(26265325L, "멤버", "이세웅","코콬","sgsg","sgsdgsd"));
        member = memberRepository.save(new Member(25325L, "간사", "권민우","캬캬","sgsgsdg","sgsdgsds"));
        notification = notificationRepository.save(new Notification("동서울",member,"안녕","반가워",LocalDate.of(2025,2,10),LocalDate.of(2025,2,15)));
        notificationPhotoRepository.save(new NotificationPhoto(notification,"/456"));
        notificationPhotoRepository.save(new NotificationPhoto(notification,"/126"));

        for(int i=0; i<50;i++) {
            if(i%5==0) {
                notification = notificationRepository.save(new Notification("학교",member,"안녕"+String.valueOf(i),"반가워",LocalDate.of(2025,2,10),LocalDate.of(2025,2,15)));
                notificationUniversityRepository.save(new NotificationUniversity(notification,University.KONKUK));
                notificationPhotoRepository.save(new NotificationPhoto(notification,"/547"));
            }
            else {
                notification = notificationRepository.save(new Notification("동서울",member,"안녕"+String.valueOf(i),"반가워",LocalDate.of(2025,2,10),LocalDate.of(2025,2,15)));
                notificationPhotoRepository.save(new NotificationPhoto(notification,"/"));
            }
        }
    }
}
