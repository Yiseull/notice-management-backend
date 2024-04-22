package com.noticemanagement.notice.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.noticemanagement.notice.api.dto.response.NoticeInfo;
import com.noticemanagement.notice.api.dto.response.NoticeResponse;
import com.noticemanagement.notice.api.dto.response.NoticesResponse;
import com.noticemanagement.notice.dao.FileRepository;
import com.noticemanagement.notice.dao.NoticeRepository;
import com.noticemanagement.notice.domain.Notice;
import com.noticemanagement.notice.fixtures.builder.NoticeBuilder;
import com.noticemanagement.notice.fixtures.setup.NoticeSetUp;

@Transactional
@SpringBootTest
class NoticeServiceTest {

	@Autowired
	NoticeService noticeService;

	@Autowired
	NoticeRepository noticeRepository;

	@Autowired
	FileRepository fileRepository;

	@Autowired
	NoticeSetUp noticeSetUp;

	@Test
	void 공지사항을_등록한다() {
		// given
		Notice notice = NoticeBuilder.build();

		// when
		Long result = noticeService.createNotice(notice, null);

		// then
		assertThat(result).isNotNull();
	}

	@Test
	void 공지사항을_수정한다() {
		// given
		Notice notice = noticeSetUp.save(NoticeBuilder.build());
		String title = "수정된 제목";
		String content = "수정된 내용";

		// when
		noticeService.modifyNotice(notice.getId(), title, content, null);

		// then
		assertThat(notice.getTitle().getTitle()).isEqualTo(title);
		assertThat(notice.getContent()).isEqualTo(content);
	}

	@Test
	void 공지사항을_삭제한다() {
		// given
		Notice notice = noticeSetUp.save(NoticeBuilder.build());

		// when
		noticeService.deleteNotice(notice.getId());

		// then
		assertThat(noticeRepository.findById(notice.getId())).isEmpty();
	}

	@Test
	void 공지사항을_조회한다() {
		// given
		Notice notice = noticeSetUp.save(NoticeBuilder.build());
		long views = notice.getViews();

		// when
		NoticeResponse result = noticeService.getNotice(notice.getId());

		// then
		assertThat(result.notice()).isEqualTo(NoticeInfo.from(notice));
		assertThat(result.notice().views()).isEqualTo(views + 1);
	}

	@Test
	void 공지사항을_모두_조회한다() {
		// given
		Notice notice1 = noticeSetUp.save(NoticeBuilder.build("제목1", "내용1", "작성자1"));
		Notice notice2 = noticeSetUp.save(NoticeBuilder.build("제목2", "내용2", "작성자2"));

		// when
		NoticesResponse result = noticeService.getNotices();

		// then
		assertThat(result.notices()).hasSize(2);
		assertThat(result.notices().get(0)).isEqualTo(NoticeInfo.from(notice1));
		assertThat(result.notices().get(1)).isEqualTo(NoticeInfo.from(notice2));
	}
}
