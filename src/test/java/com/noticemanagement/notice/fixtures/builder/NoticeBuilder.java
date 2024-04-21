package com.noticemanagement.notice.fixtures.builder;

import java.time.LocalDateTime;

import org.springframework.test.util.ReflectionTestUtils;

import com.noticemanagement.notice.domain.Notice;

public class NoticeBuilder {
	public static Notice build() {
		return Notice.builder()
			.title("title")
			.content("content")
			.startTime(LocalDateTime.of(2024, 4, 19, 13, 45))
			.endTime(LocalDateTime.of(2024, 4, 20, 13, 45))
			.writer("writer")
			.build();
	}

	public static Notice build(final Long noticeId) {
		Notice notice = Notice.builder()
			.title("title")
			.content("content")
			.startTime(LocalDateTime.of(2024, 4, 19, 13, 45))
			.endTime(LocalDateTime.of(2024, 4, 20, 13, 45))
			.writer("writer")
			.build();
		setId(notice, noticeId);
		return notice;
	}

	private static void setId(final Notice notice, final Long noticeId) {
		ReflectionTestUtils.setField(notice, "id", noticeId);
	}
}
