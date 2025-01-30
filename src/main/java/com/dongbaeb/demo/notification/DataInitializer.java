package com.dongbaeb.demo.notification;

import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.member.repository.MemberRepository;
import com.dongbaeb.demo.notification.domain.Notification;
import com.dongbaeb.demo.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public void run(String... args) {
        Member member;
        member = memberRepository.save(new Member(32532523L, "멤버", "이세영","쿄쿄","fegweg","ggs"));
        notificationRepository.save(new Notification("안녕","단녕",member, University.SIRIB,false));
        memberRepository.save(new Member(26265325L, "멤버", "이세웅","코콬","sgsg","sgsdgsd"));
        member = memberRepository.save(new Member(25325L, "간사", "권민우","캬캬","sgsgsdg","sgsdgsds"));
        notificationRepository.save(new Notification("공지에요","이것은 공지입니다.",member,null,true));

    }
}
