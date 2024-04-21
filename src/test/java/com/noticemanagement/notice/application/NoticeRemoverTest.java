package com.noticemanagement.notice.application;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.noticemanagement.notice.dao.NoticeRepository;
import com.noticemanagement.notice.domain.Notice;
import com.noticemanagement.notice.fixtures.builder.NoticeBuilder;

@ExtendWith(MockitoExtension.class)
class NoticeRemoverTest {

	@InjectMocks
	NoticeRemover noticeRemover;

	@Mock
	NoticeReader noticeReader;

	@Mock
	NoticeRepository noticeRepository;

	@Mock
	FileManager fileManager;

	@Test
	void 공지사항을_삭제한다() {
		// given
		Long noticeId = 1L;
		Notice notice = NoticeBuilder.build(noticeId);

		given(noticeReader.read(noticeId)).willReturn(notice);

		// when
		noticeRemover.remove(noticeId);

		// verify
		then(noticeRepository).should().delete(notice);
		then(fileManager).should().deleteAllByNoticeId(noticeId);
	}
}