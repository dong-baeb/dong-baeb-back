package com.dongbaeb.demo.notification;

import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.member.repository.MemberRepository;
import com.dongbaeb.demo.notification.domain.Notice;
import com.dongbaeb.demo.notification.domain.NoticePhoto;
import com.dongbaeb.demo.notification.domain.NoticeUniversity;
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
        Notice notice;
        member = memberRepository.save(new Member(32532523L, "멤버", "이세영","쿄쿄","fegweg","ggs"));
        notice = notificationRepository.save(new Notice("학교",member,"안녕","반가워",LocalDate.of(2025,2,10),LocalDate.of(2025,2,15)));
        notificationPhotoRepository.save(new NoticePhoto(notice,"/123"));
        notificationUniversityRepository.save(new NoticeUniversity(notice,University.SIRIB));

        memberRepository.save(new Member(26265325L, "멤버", "이세웅","코콬","sgsg","sgsdgsd"));
        member = memberRepository.save(new Member(25325L, "간사", "권민우","캬캬","sgsgsdg","sgsdgsds"));
        notice = notificationRepository.save(new Notice("동서울",member,"안녕","반가워",LocalDate.of(2025,2,10),LocalDate.of(2025,2,15)));
        notificationPhotoRepository.save(new NoticePhoto(notice,"/456"));
        notificationPhotoRepository.save(new NoticePhoto(notice,"/126"));

        for(int i=0; i<50;i++) {
            if(i%5==0) {
                notice = notificationRepository.save(new Notice("학교",member,"안녕"+String.valueOf(i),"반가워",LocalDate.of(2025,2,10),LocalDate.of(2025,2,15)));
                notificationUniversityRepository.save(new NoticeUniversity(notice,University.KONKUK));
                notificationPhotoRepository.save(new NoticePhoto(notice,"/547"));
            }
            else {
                notice = notificationRepository.save(new Notice("동서울",member,"안녕"+String.valueOf(i),"반가워",LocalDate.of(2025,2,10),LocalDate.of(2025,2,15)));
                notificationPhotoRepository.save(new NoticePhoto(notice,"/"));
            }
        }
    }
}
