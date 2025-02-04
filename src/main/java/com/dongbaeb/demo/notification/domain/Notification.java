package com.dongbaeb.demo.notification.domain;

import com.dongbaeb.demo.global.entity.BaseEntity;
import com.dongbaeb.demo.global.exception.BadRequestException;
import com.dongbaeb.demo.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationCategory notificationCategory;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    public Notification(String category, Member author, String title, String content, LocalDate start, LocalDate end) {
        validateDate(start, end);
        this.notificationCategory = NotificationCategory.form(category);
        this.author = author;
        this.title = title;
        this.content = content;
        this.startDate = start;
        this.endDate = end;
    }

    private void validateDate(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new BadRequestException("시작 날짜는 끝 날짜보다 클 수 없습니다.");
        }
    }

    public boolean isRoleAllowed() {
        return notificationCategory.isRoleAllowed(author.getRole());
    }

    public boolean isValidUniversityCount(int universityCount) {
        return notificationCategory.isValidUniversityCount(universityCount);
    }

    public boolean isStartDateBefore(LocalDate localDate) {
        return startDate.isBefore(localDate);
    }
}
