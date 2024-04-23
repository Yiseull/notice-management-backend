package com.noticemanagement.notice.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.noticemanagement.notice.domain.Notice;

import lombok.RequiredArgsConstructor;

/**
 * 공지사항을 수정하는 기능을 담당하는 클래스입니다.
 */
@Component
@RequiredArgsConstructor
public class NoticeModifier {

	private final NoticeReader noticeReader;
	private final FileManager fileManager;

	@Transactional
	public void modify(
		final Long noticeId,
		final String title,
		final String content,
		final List<MultipartFile> multipartFiles
	) {
		final Notice notice = noticeReader.read(noticeId);
		notice.modify(title, content);
		fileManager.modify(multipartFiles, notice.getId());
	}
}
