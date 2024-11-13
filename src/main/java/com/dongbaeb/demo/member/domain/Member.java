package com.dongbaeb.demo.member.domain;

import com.dongbaeb.demo.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long kakaoId;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    private String profileImageUrl;

    private String studentNo;

    @Builder
    public Member(Long kakaoId, String role, String name, String nickname, String studentNo) {
        this.kakaoId = kakaoId;
        this.role = role;
        this.name = name;
        this.nickname = nickname;
        this.studentNo = studentNo;
    }

    public void update(String role, String name, String nickname, String studentNo) {
        this.role = role;
        this.name = name;
        this.nickname = nickname;
        this.studentNo = studentNo;
    }
}
