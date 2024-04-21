package com.noticemanagement.notice.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.noticemanagement.notice.domain.Notice;
import com.noticemanagement.notice.fixtures.builder.NoticeBuilder;

@ExtendWith(MockitoExtension.class)
class NoticeModifierTest {

	@InjectMocks
	NoticeModifier noticeModifier;

	@Mock
	NoticeReader noticeReader;

	@Mock
	FileManager fileManager;

	@Test
	void 공지사항을_수정한다() {
		// given
		Long noticeId = 1L;
		String title = "new title";
		String content = "new content";
		Notice notice = NoticeBuilder.build(noticeId);

		given(noticeReader.read(noticeId)).willReturn(notice);

		willDoNothing().given(fileManager).modify(null, noticeId);

		// when
		noticeModifier.modify(noticeId, title, content, null);

		// then
		assertThat(notice.getTitle().getTitle()).isEqualTo(title);
		assertThat(notice.getContent()).isEqualTo(content);
	}
}