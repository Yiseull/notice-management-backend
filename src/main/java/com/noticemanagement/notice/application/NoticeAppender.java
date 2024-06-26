package com.noticemanagement.notice.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.noticemanagement.notice.dao.NoticeRepository;
import com.noticemanagement.notice.domain.Notice;

import lombok.RequiredArgsConstructor;

/**
 * 공지사항을 추가하는 기능을 담당하는 클래스입니다.
 */
@Component
@RequiredArgsConstructor
public class NoticeAppender {

	private final NoticeRepository noticeRepository;
	private final FileManager fileManager;

	@Transactional
	public Notice append(final Notice notice, final List<MultipartFile> multipartFiles) {
		final Notice savedNotice = noticeRepository.save(notice);
		fileManager.append(multipartFiles, savedNotice.getId());
		return savedNotice;
	}
}
