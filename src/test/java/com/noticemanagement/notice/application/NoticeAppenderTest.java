package com.noticemanagement.notice.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.noticemanagement.notice.dao.NoticeRepository;
import com.noticemanagement.notice.domain.Notice;
import com.noticemanagement.notice.fixtures.builder.NoticeBuilder;

@ExtendWith(MockitoExtension.class)
class NoticeAppenderTest {

	@InjectMocks
	NoticeAppender noticeAppender;

	@Mock
	NoticeRepository noticeRepository;

	@Mock
	FileManager fileManager;

	@Test
	void 공지사항을_등록한다() {
		// given
		Notice notice = NoticeBuilder.build();

		given(noticeRepository.save(any(Notice.class))).willReturn(notice);

		willDoNothing().given(fileManager).append(null, notice.getId());

		// when
		Notice result = noticeAppender.append(notice, null);

		// then
		assertThat(result)
			.usingRecursiveComparison()
			.ignoringFields("id")
			.isEqualTo(notice);
	}
}