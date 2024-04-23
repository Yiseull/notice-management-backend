package com.noticemanagement.notice.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.noticemanagement.global.error.exception.EntityNotFoundException;
import com.noticemanagement.global.error.exception.ErrorCode;
import com.noticemanagement.notice.dao.NoticeRepository;
import com.noticemanagement.notice.domain.Notice;

import lombok.RequiredArgsConstructor;

/**
 * 공지사항을 조회하는 기능을 담당하는 클래스입니다.
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeReader {

	private final NoticeRepository noticeRepository;

	public Notice read(final Long noticeId) {
		return noticeRepository.findById(noticeId)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOTICE_NOT_FOUND));
	}

	public Notice readWithLock(final Long noticeId) {
		return noticeRepository.findByIdWithPessimisticLock(noticeId);
	}

	public List<Notice> readAll() {
		return noticeRepository.findAll();
	}
}
