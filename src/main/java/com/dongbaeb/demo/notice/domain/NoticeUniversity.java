
package com.dongbaeb.demo.notice.domain;

import com.dongbaeb.demo.member.domain.University;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class NoticeUniversity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "notice_id", nullable = false)
    private Notice notice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private University university;

    public NoticeUniversity(Notice notice, University university) {
        this.notice = notice;
        this.university = university;
    }
}