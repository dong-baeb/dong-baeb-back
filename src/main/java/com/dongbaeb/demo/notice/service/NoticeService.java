package com.dongbaeb.demo.notice.service;

import com.dongbaeb.demo.global.dto.MemberAuth;
import com.dongbaeb.demo.global.exception.ForbiddenException;
import com.dongbaeb.demo.global.exception.ResourceNotFoundException;
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
    private final NoticeRepository noticeRepository;
    private final NoticePhotoRepository noticePhotoRepository;
    private final NoticeUniversityRepository noticeUniversityRepository;

    @Transactional(readOnly = true)
    public NoticeResponse readNotice(Long id) {
        Notice notice = findNoticeById(id);
        List<NoticePhoto> photos = noticePhotoRepository.findByNoticeId(id);
        List<NoticeUniversity> universities = noticeUniversityRepository.findByNoticeId(id);

        return NoticeResponse.from(notice, photos, universities);
    }

    @Transactional
    public void deleteNotice(Long id, MemberAuth memberAuth) {
        Notice notice = findNoticeById(id);
        validateAuthorization(notice, memberAuth);

        noticeUniversityRepository.deleteByNotice(notice);
        noticePhotoRepository.deleteByNotice(notice);
        noticeRepository.delete(notice);
    }

    private Notice findNoticeById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 id를 가진 공지를 찾을 수 없습니다." + id));
    }

    private void validateAuthorization(Notice notice, MemberAuth memberAuth) {
        if (!notice.getAuthor().getId().equals(memberAuth.memberId())) { // 관리자 권한 추가?
            throw new ForbiddenException("공지 삭제 권한이 없습니다.");
        }
    }
}
