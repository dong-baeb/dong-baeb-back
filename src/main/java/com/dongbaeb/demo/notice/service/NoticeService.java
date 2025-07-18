package com.dongbaeb.demo.notice.service;

import com.dongbaeb.demo.global.dto.MemberAuth;
import com.dongbaeb.demo.global.exception.BadRequestException;
import com.dongbaeb.demo.global.exception.ForbiddenException;
import com.dongbaeb.demo.global.exception.ResourceNotFoundException;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.MemberUniversity;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.member.repository.MemberRepository;
import com.dongbaeb.demo.member.repository.MemberUniversityRepository;
import com.dongbaeb.demo.notice.domain.Notice;
import com.dongbaeb.demo.notice.domain.NoticeCategory;
import com.dongbaeb.demo.notice.domain.NoticePhoto;
import com.dongbaeb.demo.notice.domain.NoticeUniversity;
import com.dongbaeb.demo.notice.dto.NoticeRequest;
import com.dongbaeb.demo.notice.dto.NoticeResponse;
import com.dongbaeb.demo.notice.repository.NoticePhotoRepository;
import com.dongbaeb.demo.notice.repository.NoticeRepository;
import com.dongbaeb.demo.notice.repository.NoticeUniversityRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
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

    @Transactional(readOnly = true)
    public NoticeResponse readNotice(Long id, MemberAuth memberAuth) {
        Member member = findMemberById(memberAuth.memberId());
        Notice notice = findNoticeById(id);
        List<NoticePhoto> photos = noticePhotoRepository.findByNoticeId(id);
        List<NoticeUniversity> noticeUniversities = noticeUniversityRepository.findByNoticeId(id);
        validateReadAuthorization(member, notice, noticeUniversities);

        return NoticeResponse.from(notice, photos, noticeUniversities);
    }

    @Transactional(readOnly = true)
    public List<NoticeResponse> readUpcomingNotice(MemberAuth memberAuth) {
        List<Notice> upcomingNotices = new ArrayList<>();
        upcomingNotices.addAll(
                noticeRepository.findByEastUniversityAndDateRange(LocalDate.now(), LocalDate.now().plusWeeks(2)));
        upcomingNotices.addAll(getUpcomingNoticeByUniversity(memberAuth));

        upcomingNotices.sort(Comparator.comparing(Notice::getStartDate));

        return upcomingNotices.stream()
                .map(notice -> NoticeResponse.fromUpcomingNotice(notice))
                .toList();
    }

    private List<Notice> getUpcomingNoticeByUniversity(MemberAuth memberAuth) {
        Member member = findMemberById(memberAuth.memberId());
        List<University> universityList = memberUniversityRepository.findByMember(member)
                .stream()
                .map(memberUniversity -> memberUniversity.getUniversity())
                .toList();
        List<Notice> upcomingUniversityNotices = noticeUniversityRepository.findByUniversitiesAndDateRange(
                        universityList,
                        LocalDate.now(), LocalDate.now().plusWeeks(2))
                .stream()
                .map(noticeUniversity -> noticeUniversity.getNotice())
                .toList();
        return upcomingUniversityNotices;
    }

    public List<NoticeResponse> getNoticeByMemberId(MemberAuth memberAuth) {
        List<NoticeResponse> noticeResponses = new ArrayList<>();

        Member author = findMemberById(memberAuth.memberId());
        List<Notice> notices = noticeRepository.findByAuthor(author);
        for (Notice notice : notices) {
            List<NoticePhoto> noticePhotos = noticePhotoRepository.findByNotice(notice);
            List<NoticeUniversity> noticeUniversities = noticeUniversityRepository.findByNotice(notice);
            noticeResponses.add(NoticeResponse.from(notice, noticePhotos, noticeUniversities));
        }

        return noticeResponses;
    }

    @Transactional
    public void deleteNotice(Long id, MemberAuth memberAuth) {
        Member member = findMemberById(memberAuth.memberId());
        Notice notice = findNoticeById(id);
        validateDeleteAuthorization(member, notice);
        noticeUniversityRepository.deleteByNotice(notice);
        noticePhotoRepository.deleteByNotice(notice);
        noticeRepository.delete(notice);
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

    public List<NoticeResponse> getAllCouncilsNotification(Pageable pageable) {
        List<NoticeResponse> noticeResponses = new ArrayList<>();
        List<Notice> notices = noticeRepository.findByNoticeCategoryOrderByIdAsc(
                NoticeCategory.EAST_SEOUL,
                pageable).getContent();

        for (int i = 0; i < notices.size(); i++) {
            Notice notice = notices.get(i);
            List<NoticePhoto> noticePhotos = noticePhotoRepository.findByNotice(notice);
            noticeResponses.add(NoticeResponse.from(notice, noticePhotos, new ArrayList<>()));
        }

        return noticeResponses;
    }

    public List<NoticeResponse> getByUniversityName(University name, Pageable pageable, MemberAuth memberAuth) {

        validateUniversity(memberAuth.memberId(), name);

        List<NoticeResponse> noticeResponses = new ArrayList<>();

        List<NoticeUniversity> noticeUniversities = noticeUniversityRepository.findByUniversity(name, pageable)
                .getContent();

        for (int i = 0; i < noticeUniversities.size(); i++) {
            Notice notice = noticeUniversities.get(i).getNotice();
            List<NoticePhoto> noticePhotos = noticePhotoRepository.findByNotice(notice);
            List<NoticeUniversity> universities = noticeUniversityRepository.findByNotice(notice);
            noticeResponses.add(NoticeResponse.from(notice, noticePhotos, universities));
        }

        return noticeResponses;
    }

    private void validateUniversity(Long memberId, University name) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 아이디를 가진 사용자를 찾을 수 없습니다: " + memberId));
        if (!isExistUniversity(member, name)) {
            throw new ForbiddenException("다른 대학교의 공지에 접근할 수 없습니다.");
        }
    }

    private boolean isExistUniversity(Member member, University name) {
        return memberUniversityRepository.existsByMemberAndUniversity(member, name);
    }

    private Notice findNoticeById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 id를 가진 공지를 찾을 수 없습니다." + id));
    }

    private List<University> findMemberUniversitiesByMember(Member member) {
        return memberUniversityRepository.findByMember(member)
                .stream()
                .map(MemberUniversity::getUniversity)
                .toList();
    }

    private void validateReadAuthorization(Member member, Notice notice, List<NoticeUniversity> noticeUniversities) {
        if (!member.isRole("간사") && !isAuthorizedNoticeUniversity(member, notice, noticeUniversities)) {
            throw new ForbiddenException("공지 조회 권한이 없습니다.");
        }
    }

    private void validateDeleteAuthorization(Member member, Notice notice) {
        if (!notice.getAuthor().getId().equals(member.getId())) {
            throw new ForbiddenException("공지 삭제 권한이 없습니다.");
        }
    }

    private boolean isAuthorizedNoticeUniversity(Member member, Notice notice,
                                                 List<NoticeUniversity> noticeUniversities) {
        return notice.isEastSeoulCategory() || isMemberBelongToUniversity(member, noticeUniversities);
    }

    private boolean isMemberBelongToUniversity(Member member, List<NoticeUniversity> noticeUniversities) {
        List<University> memberUniversities = findMemberUniversitiesByMember(member);
        return noticeUniversities.stream()
                .anyMatch(university -> memberUniversities.contains(university.getUniversity()));
    }

}
