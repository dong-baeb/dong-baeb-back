package com.dongbaeb.demo.profile.entity;

import com.dongbaeb.demo.profile.dto.MemberRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberUniversity> memberUniversities;

    public void update(MemberRequest memberRequest) {
        this.kakaoId = memberRequest.kakaoId();
        this.role = memberRequest.role();
        this.name = memberRequest.name();
        this.nickname = memberRequest.nickname();
        this.profileImageUrl = memberRequest.profileImageUrl();
        this.studentNo = memberRequest.studentNo();
    }
}
