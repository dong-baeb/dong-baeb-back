package com.dongbaeb.demo.notification.domain;

import com.dongbaeb.demo.global.entity.BaseEntity;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.University;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    @Enumerated(EnumType.STRING)
    private University university;

    //전체 공지인지 여부 (대학교와 합칠 수 있지 않을까?)
    @Column
    private Boolean isWholeNotification;

    //사진 url??

    public Notification(String title, String content,Member member,University university,Boolean isWholeNotification) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.university = university;
        this.isWholeNotification = isWholeNotification;
    }
}
