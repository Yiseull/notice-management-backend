package com.noticemanagement.notice.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class NoticeTest {
	@Test
	void 공지사항을_수정한다() {
		// given
		final Notice notice = Notice.builder()
			.title("title")
			.content("content")
			.startTime(LocalDateTime.of(2024, 4, 19, 13, 45))
			.endTime(LocalDateTime.of(2024, 4, 20, 13, 45))
			.writer("writer")
			.build();
		final String title = "title2";
		final String content = "content2";

		// when
		notice.modify(title, content);

		// then
		assertThat(notice.getTitle().getTitle()).isEqualTo(title);
		assertThat(notice.getContent()).isEqualTo(content);
	}
}
