package com.noticemanagement.notice.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.noticemanagement.global.error.exception.EntityNotFoundException;
import com.noticemanagement.global.error.exception.ErrorCode;
import com.noticemanagement.notice.dao.NoticeRepository;
import com.noticemanagement.notice.domain.Notice;
import com.noticemanagement.notice.fixtures.builder.NoticeBuilder;

@ExtendWith(MockitoExtension.class)
class NoticeReaderTest {

	@InjectMocks
	NoticeReader noticeReader;

	@Mock
	NoticeRepository noticeRepository;

	@Nested
	class read {
		@Test
		void 공지사항을_조회한다() {
			// given
			Long noticeId = 1L;
			Notice notice = NoticeBuilder.build(noticeId);

			given(noticeRepository.findById(noticeId)).willReturn(Optional.of(notice));

			// when
			Notice result = noticeReader.read(noticeId);

			// then
			assertThat(result).isEqualTo(notice);
		}

		@Test
		void 존재하지_않는_공지사항을_조회하면_예외가_발생한다() {
			// given
			Long noticeId = 1L;

			// when & then
			assertThatThrownBy(() -> noticeReader.read(noticeId))
				.isInstanceOf(EntityNotFoundException.class)
				.hasMessage(ErrorCode.NOTICE_NOT_FOUND.getMessage());
		}
	}

	@Test
	void 공지사항을_모두_조회한다() {
		// given
		Notice notice1 = NoticeBuilder.build(1L);
		Notice notice2 = NoticeBuilder.build(2L);
		Notice notice3 = NoticeBuilder.build(3L);
		List<Notice> notices = List.of(notice1, notice2, notice3);

		given(noticeRepository.findAll()).willReturn(notices);

		// when
		List<Notice> result = noticeReader.readAll();

		// then
		assertThat(result)
			.hasSize(3)
			.contains(notice1, notice2, notice3);
	}
}
