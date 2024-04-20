package com.noticemanagement.notice.fixtures.setup;

import org.springframework.stereotype.Component;

import com.noticemanagement.notice.dao.NoticeRepository;
import com.noticemanagement.notice.domain.Notice;

@Component
public class NoticeSetUp {

	private final NoticeRepository noticeRepository;

	public NoticeSetUp(NoticeRepository noticeRepository) {
		this.noticeRepository = noticeRepository;
	}

	public Notice save(Notice notice) {
		return noticeRepository.save(notice);
	}
}
