package com.noticemanagement.notice.api.dto.request;

import java.time.LocalDateTime;

import com.noticemanagement.notice.domain.Notice;

public record NoticeCreateRequest(
	String title,
	String content,
	LocalDateTime startTime,
	LocalDateTime endTime,
	String writer
) {
	public Notice toNotice() {
		return Notice.builder()
			.title(title)
			.content(content)
			.startTime(startTime)
			.endTime(endTime)
			.writer(writer)
			.build();
	}
}
