package com.dongbaeb.demo.member.domain;

import com.dongbaeb.demo.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    private String profileImageUrl;

    private String studentNo;

    public Member(Long kakaoId, String role, String name, String nickname, String profileImageUrl, String studentNo) {
        this.kakaoId = kakaoId;
        this.role = Role.from(role);
        this.name = name;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.studentNo = studentNo;
    }

    public void update(String role, String name, String nickname, String studentNo) {
        this.role = Role.from(role);
        this.name = name;
        this.nickname = nickname;
        this.studentNo = studentNo;
    }

    public boolean isValidUniversityCount(int universityCount) {
        return role.isValidUniversityCount(universityCount);
    }

    public boolean isLeader() {
        return role.equals(Role.LEADER);
    }
}
