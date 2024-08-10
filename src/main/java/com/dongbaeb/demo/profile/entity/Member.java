package com.dongbaeb.demo.profile.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String kakaoId;
    private String role;
    private String name;
    private String nickname;
    private String profileImageUrl;
    private String studentNo;

    @ManyToMany
    @JoinTable(
            name = "member_university",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "university_id")
    )
    private List<University> universities;

    public void setKakaoId(String kakaoId) {
        this.kakaoId = kakaoId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public void setUniversities(List<University> universities) {
        this.universities = universities;
    }

}
