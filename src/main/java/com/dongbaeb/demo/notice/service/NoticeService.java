package com.dongbaeb.demo.notice.service;

import com.dongbaeb.demo.global.dto.MemberAuth;
import com.dongbaeb.demo.global.exception.ForbiddenException;
import com.dongbaeb.demo.global.exception.ResourceNotFoundException;
import com.dongbaeb.demo.member.domain.Member;
import com.dongbaeb.demo.member.domain.MemberUniversity;
import com.dongbaeb.demo.member.domain.University;
import com.dongbaeb.demo.member.repository.MemberRepository;
import com.dongbaeb.demo.member.repository.MemberUniversityRepository;
import com.dongbaeb.demo.notice.domain.Notice;
import com.dongbaeb.demo.notice.domain.NoticePhoto;
import com.dongbaeb.demo.notice.domain.NoticeUniversity;
import com.dongbaeb.demo.notice.dto.NoticeResponse;
import com.dongbaeb.demo.notice.repository.NoticeRepository;
import com.dongbaeb.demo.notice.repository.NoticePhotoRepository;
import com.dongbaeb.demo.notice.repository.NoticeUniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final MemberRepository memberRepository;
    private final MemberUniversityRepository memberUniversityRepository;
    private final NoticeRepository noticeRepository;
    private final NoticePhotoRepository noticePhotoRepository;
    private final NoticeUniversityRepository noticeUniversityRepository;

    @Transactional(readOnly = true)
    public NoticeResponse readNotice(Long id, MemberAuth memberAuth) {
        Member member = findMemberById(memberAuth.memberId());
        Notice notice = findNoticeById(id);
        List<NoticePhoto> photos = noticePhotoRepository.findByNoticeId(id);
        List<NoticeUniversity> noticeUniversities = noticeUniversityRepository.findByNoticeId(id);
        validateReadAuthorization(member, notice, noticeUniversities);

        return NoticeResponse.from(notice, photos, noticeUniversities);
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
                .orElseThrow(() -> new ResourceNotFoundException("해당 id를 가진 멤버를 찾을 수 없습니다." + id));
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

    private boolean isAuthorizedNoticeUniversity(Member member, Notice notice, List<NoticeUniversity> noticeUniversities) {
        return notice.isEastSeoulCategory() || isMemberBelongToUniversity(member, noticeUniversities);
    }

    private boolean isMemberBelongToUniversity(Member member, List<NoticeUniversity> noticeUniversities) {
        List<University> memberUniversities = findMemberUniversitiesByMember(member);
        return noticeUniversities.stream()
                .anyMatch(university -> memberUniversities.contains(university.getUniversity()));
    }
}
