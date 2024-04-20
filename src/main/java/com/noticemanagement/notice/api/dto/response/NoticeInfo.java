package com.noticemanagement.notice.api.dto.response;

import java.time.LocalDateTime;

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
}
