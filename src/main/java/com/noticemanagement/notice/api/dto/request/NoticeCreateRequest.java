package com.noticemanagement.notice.api.dto.request;

import java.time.LocalDateTime;

import com.noticemanagement.notice.domain.Notice;

import io.swagger.v3.oas.annotations.media.Schema;

public record NoticeCreateRequest(
	@Schema(description = "제목", example = "공지사항 제목")
	String title,

	@Schema(description = "내용", example = "공지사항 내용")
	String content,

	@Schema(description = "시작 시간", example = "2024-07-01T00:00:00")
	LocalDateTime startTime,

	@Schema(description = "종료 시간", example = "2024-07-31T23:59:59")
	LocalDateTime endTime,

	@Schema(description = "작성자", example = "작성자")
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
