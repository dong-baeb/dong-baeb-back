package com.dongbaeb.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
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
}
