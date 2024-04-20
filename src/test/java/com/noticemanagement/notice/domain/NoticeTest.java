package com.noticemanagement.notice.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class NoticeTest {

	Notice notice = Notice.builder()
		.title("title")
		.content("content")
		.startTime(LocalDateTime.of(2024, 4, 19, 13, 45))
		.endTime(LocalDateTime.of(2024, 4, 20, 13, 45))
		.writer("writer")
		.build();

	@Test
	void 공지사항을_수정한다() {
		// given
		String title = "title2";
		String content = "content2";

		// when
		notice.modify(title, content);

		// then
		assertThat(notice.getTitle().getTitle()).isEqualTo(title);
		assertThat(notice.getContent()).isEqualTo(content);
	}

	@Test
	void 조회수를_증가시킨다() {
		// given
		long views = notice.getViews();

		// when
		notice.increaseViews();

		// then
		assertThat(notice.getViews()).isEqualTo(views + 1);
	}
}
