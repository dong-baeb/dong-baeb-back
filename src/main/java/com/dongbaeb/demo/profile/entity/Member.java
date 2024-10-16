package com.dongbaeb.demo.profile.entity;

import com.dongbaeb.demo.global.entity.BaseEntity;
import com.dongbaeb.demo.profile.dto.MemberRequest;
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
    private static final int MAX_NAME_LENGTH = 5;
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

    // TODO: 엔티티에 DTO 넘어오지 않게 수정하기
    public void update(MemberRequest memberRequest) {
        this.role = memberRequest.role();
        this.name = memberRequest.name();
        this.nickname = memberRequest.nickname();
        this.profileImageUrl = memberRequest.profileImageUrl();
        this.studentNo = memberRequest.studentNo();
    }
}
