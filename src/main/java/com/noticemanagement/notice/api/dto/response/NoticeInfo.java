package com.noticemanagement.notice.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.noticemanagement.notice.domain.Notice;

public record NoticeInfo(
	Long noticeId,
	String title,
	String content,
	LocalDateTime createdAt,
	String writer,
	long views
) {
	public static NoticeInfo from(final Notice notice) {
		return new NoticeInfo(
			notice.getId(),
			notice.getTitle().getTitle(),
			notice.getContent(),
			notice.getCreatedAt(),
			notice.getWriter(),
			notice.getViews()
		);
	}

	public static List<NoticeInfo> listOf(final List<Notice> notices) {
		return notices.stream()
			.map(NoticeInfo::from)
			.toList();
	}
}
