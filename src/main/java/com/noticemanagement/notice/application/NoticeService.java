package com.noticemanagement.notice.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.noticemanagement.notice.api.dto.response.FileInfo;
import com.noticemanagement.notice.api.dto.response.NoticeInfo;
import com.noticemanagement.notice.api.dto.response.NoticeResponse;
import com.noticemanagement.notice.api.dto.response.NoticesResponse;
import com.noticemanagement.notice.domain.File;
import com.noticemanagement.notice.domain.Notice;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeService {

	private final NoticeAppender noticeAppender;
	private final NoticeReader noticeReader;
	private final NoticeModifier noticeModifier;
	private final NoticeRemover noticeRemover;
	private final FileManager fileManager;

	public Long createNotice(final Notice notice, final List<MultipartFile> multipartFiles) {
		final Notice savedNotice = noticeAppender.append(notice, multipartFiles);
		return savedNotice.getId();
	}

	public void modifyNotice(
		final Long noticeId,
		final String title,
		final String content,
		final List<MultipartFile> multipartFiles
	) {
		noticeModifier.modify(noticeId, title, content, multipartFiles);
	}

	public void deleteNotice(final Long noticeId) {
		noticeRemover.remove(noticeId);
	}

	@Transactional
	public NoticeResponse getNotice(final Long noticeId) {
		final Notice notice = noticeReader.read(noticeId);
		notice.increaseViews();
		final List<File> files = fileManager.readAllByNoticeId(noticeId);
		return new NoticeResponse(NoticeInfo.from(notice), FileInfo.listOf(files));
	}

	public NoticesResponse getNotices() {
		final List<Notice> notices = noticeReader.readAll();
		return new NoticesResponse(NoticeInfo.listOf(notices));
	}
}
