package com.noticemanagement.notice.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.noticemanagement.notice.dao.NoticeRepository;
import com.noticemanagement.notice.domain.Notice;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NoticeRemover {

	private final NoticeReader noticeReader;
	private final NoticeRepository noticeRepository;
	private final FileManager fileManager;

	@Transactional
	public void remove(final Long noticeId) {
		final Notice notice = noticeReader.read(noticeId);
		noticeRepository.delete(notice);
		fileManager.deleteAllByNoticeId(noticeId);
	}
}
