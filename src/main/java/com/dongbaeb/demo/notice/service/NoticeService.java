package com.dongbaeb.demo.notice.service;

import com.dongbaeb.demo.global.dto.MemberAuth;
import com.dongbaeb.demo.global.exception.BadRequestException;
import com.dongbaeb.demo.global.exception.ForbiddenException;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.member.repository.MemberRepository;
import com.dongbaeb.demo.member.repository.MemberUniversityRepository;
import com.dongbaeb.demo.notice.domain.Notice;
import com.dongbaeb.demo.notice.domain.NoticeCategory;
import com.dongbaeb.demo.notice.domain.NoticePhoto;
import com.dongbaeb.demo.notice.domain.NoticeUniversity;
import com.dongbaeb.demo.notice.dto.NoticeRequest;
import com.dongbaeb.demo.notice.repository.NoticePhotoRepository;
import com.dongbaeb.demo.notice.repository.NoticeRepository;
import com.dongbaeb.demo.notice.repository.NoticeUniversityRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticePhotoRepository noticePhotoRepository;
    private final NoticeUniversityRepository noticeUniversityRepository;
    private final MemberRepository memberRepository;
    private final MemberUniversityRepository memberUniversityRepository;

    @Transactional
    public Long createNotice(NoticeRequest request, MemberAuth memberAuth) {
        Member author = findMemberById(memberAuth.memberId());
        Notice notice = request.toNotice(author);
        List<University> universities = getUniversities(request);
        createNotice(notice, author, universities);
        createNoticePhotos(notice, request);
        createNoticeUniversities(universities, notice);

        return notice.getId();
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 멤버의 id입니다."));
    }

    private List<University> getUniversities(NoticeRequest request) {
        return request.universities()
                .stream()
                .map(University::fromName)
                .toList();
    }

    private void createNotice(Notice notice, Member author, List<University> universities) {
        validateStartDate(notice);
        validateUniversityCount(notice, universities);
        validateAuthorization(notice, author, universities);
        noticeRepository.save(notice);
    }

    private void validateStartDate(Notice notice) {
        if (notice.isNoticePast()) {
            throw new BadRequestException("과거 날짜에 대한 공지는 작성할 수 없습니다.");
        }
    }

    private void validateUniversityCount(Notice notice, List<University> universities) {
        if (!notice.isValidUniversityCount(universities.size())) {
            throw new BadRequestException("공지의 학교 개수가 올바르지 않습니다.");
        }
    }

    private void validateAuthorization(Notice notice, Member author, List<University> universities) {
        if (!notice.isRoleAllowed() || isLeaderNotBelongToUniversity(author, universities)) {
            throw new ForbiddenException("공지를 작성할 권한이 없습니다.");
        }
    }

    private boolean isLeaderNotBelongToUniversity(Member author, List<University> universities) {
        return author.isLeader() && !isAuthorBelongToAllUniversities(author, universities);
    }

    private boolean isAuthorBelongToAllUniversities(Member author, List<University> universities) {
        return universities.stream()
                .allMatch(university -> memberUniversityRepository.existsByMemberAndUniversity(author, university));
    }

    private void createNoticePhotos(Notice notice, NoticeRequest request) {
        List<NoticePhoto> noticePhotos = request.imageUrls()
                .stream()
                .map(imageUrl -> new NoticePhoto(notice, imageUrl))
                .toList();
        noticePhotoRepository.saveAll(noticePhotos);
    }

    private void createNoticeUniversities(List<University> universities, Notice notice) {
        List<NoticeUniversity> noticeUniversities = universities
                .stream()
                .map(university -> new NoticeUniversity(notice, university))
                .toList();
        noticeUniversityRepository.saveAll(noticeUniversities);
    }

    @Transactional
    public void updateNotice(Long noticeId, NoticeRequest request, MemberAuth author) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new EntityNotFoundException("공지 없음"));

        // 권한 확인 로직이 필요하다면 여기서 체크
        if (!notice.isRoleAllowed()) {
            throw new ForbiddenException("작성자만 공지를 수정할 수 있습니다.");
        }

        // 공지 내용 업데이트
        notice.update(
                request.title(),
                request.content(),
                request.startDate(),
                request.endDate(),
                NoticeCategory.from(request.category())
        );

        // 연관된 사진, 학교도 필요하면 업데이트 (예: 기존 삭제 후 다시 저장)
        // updateNoticePhotos(notice, request.imageUrls());
        // updateNoticeUniversities(notice, request.universities());

        // save는 없어도 됨. JPA가 @Transactional 안에서 dirty checking으로 자동 반영

    }


}
