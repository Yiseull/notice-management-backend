package com.noticemanagement.notice.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyList;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.noticemanagement.global.error.exception.EntityNotFoundException;
import com.noticemanagement.global.error.exception.ErrorCode;
import com.noticemanagement.notice.api.dto.response.NoticeInfo;
import com.noticemanagement.notice.api.dto.response.NoticeResponse;
import com.noticemanagement.notice.api.dto.response.NoticesResponse;
import com.noticemanagement.notice.dao.FileRepository;
import com.noticemanagement.notice.dao.NoticeRepository;
import com.noticemanagement.notice.domain.File;
import com.noticemanagement.notice.domain.Notice;

@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {

	@InjectMocks
	NoticeService noticeService;

	@Mock
	NoticeRepository noticeRepository;

	@Mock
	FileRepository fileRepository;

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(noticeService, "uploadDir", "src/test/resources/static/");
	}

	@Nested
	class CreateNotice {
		@Test
		void 첨부파일이_없는_공지사항을_등록한다() {
			// given
			Notice notice = Notice.builder()
				.title("title")
				.content("content")
				.startTime(LocalDateTime.of(2024, 4, 19, 13, 45))
				.endTime(LocalDateTime.of(2024, 4, 20, 13, 45))
				.writer("writer")
				.build();

			List<MultipartFile> multipartFiles = Collections.emptyList();

			given(noticeRepository.save(any(Notice.class))).willReturn(notice);

			// when
			noticeService.createNotice(notice, multipartFiles);

			// then
			then(noticeRepository).should().save(any(Notice.class));
		}

		@Test
		void 첨부파일이_있는_공지사항을_등록한다() throws IOException {
			// given
			Notice notice = Notice.builder()
				.title("title")
				.content("content")
				.startTime(LocalDateTime.of(2024, 4, 19, 13, 45))
				.endTime(LocalDateTime.of(2024, 4, 20, 13, 45))
				.writer("writer")
				.build();
			ReflectionTestUtils.setField(notice, "id", 1L);

			MultipartFile multipartFile1 = Mockito.mock(MultipartFile.class);
			MultipartFile multipartFile2 = Mockito.mock(MultipartFile.class);
			List<MultipartFile> multipartFiles = Arrays.asList(multipartFile1, multipartFile2);

			File file1 = new File(1L, "file1", ".png");
			File file2 = new File(1L, "file2", ".png");
			List<File> files = Arrays.asList(file1, file2);

			given(noticeRepository.save(any(Notice.class))).willReturn(notice);
			given(fileRepository.saveAll(any())).willReturn(files);

			given(multipartFile1.getOriginalFilename()).willReturn(file1.getFileName());
			given(multipartFile2.getOriginalFilename()).willReturn(file1.getFileName());
			willDoNothing().given(multipartFile1).transferTo(any(java.nio.file.Path.class));
			willDoNothing().given(multipartFile2).transferTo(any(java.nio.file.Path.class));

			// when
			Long result = noticeService.createNotice(notice, multipartFiles);

			// then
			assertThat(result).isEqualTo(notice.getId());
			then(noticeRepository).should().save(any(Notice.class));
			then(fileRepository).should().saveAll(anyList());
		}
	}

	@Nested
	class ModifyNotice {
		Notice notice = Notice.builder()
			.title("title")
			.content("content")
			.startTime(LocalDateTime.of(2024, 4, 19, 13, 45))
			.endTime(LocalDateTime.of(2024, 4, 20, 13, 45))
			.writer("writer")
			.build();

		@Test
		void 첨부파일이_없는_공지사항을_수정한다() {
			// given
			String newTitle = "title2";
			String newContent = "content2";

			List<MultipartFile> multipartFiles = Collections.emptyList();

			given(noticeRepository.findById(anyLong())).willReturn(java.util.Optional.of(notice));

			// when
			noticeService.modifyNotice(1L, newTitle, newContent, multipartFiles);

			// then
			assertThat(notice.getTitle().getTitle()).isEqualTo(newTitle);
			assertThat(notice.getContent()).isEqualTo(newContent);

			// verify
			then(noticeRepository).should().findById(1L);
		}

		@Test
		void 첨부파일이_있는_공지사항을_수정한다() throws IOException {
			// given
			String newTitle = "title2";
			String newContent = "content2";

			MultipartFile multipartFile1 = Mockito.mock(MultipartFile.class);
			MultipartFile multipartFile2 = Mockito.mock(MultipartFile.class);
			List<MultipartFile> newMultipartFiles = Arrays.asList(multipartFile1, multipartFile2);

			File file1 = new File(1L, "file1", ".png");
			File file2 = new File(1L, "file2", ".png");
			List<File> files = Arrays.asList(file1, file2);

			given(noticeRepository.findById(anyLong())).willReturn(java.util.Optional.of(notice));
			given(fileRepository.findAllByNoticeId(anyLong())).willReturn(files);
			willDoNothing().given(fileRepository).deleteAll(files);
			given(fileRepository.saveAll(any())).willReturn(files);

			given(multipartFile1.getOriginalFilename()).willReturn(file1.getFileName());
			given(multipartFile2.getOriginalFilename()).willReturn(file1.getFileName());
			willDoNothing().given(multipartFile1).transferTo(any(java.nio.file.Path.class));
			willDoNothing().given(multipartFile2).transferTo(any(java.nio.file.Path.class));

			// when
			noticeService.modifyNotice(1L, newTitle, newContent, newMultipartFiles);

			// then
			assertThat(notice.getTitle().getTitle()).isEqualTo(newTitle);
			assertThat(notice.getContent()).isEqualTo(newContent);

			// verify
			then(noticeRepository).should().findById(1L);
			then(fileRepository).should().findAllByNoticeId(1L);
			then(fileRepository).should().deleteAll(files);
			then(fileRepository).should().saveAll(anyList());
		}

		@Test
		void 공지사항이_없는_경우_예외를_발생시킨다() {
			// given
			given(noticeRepository.findById(anyLong())).willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> noticeService.modifyNotice(1L, "title2", "content2", Collections.emptyList()))
				.isInstanceOf(EntityNotFoundException.class)
				.hasMessage(ErrorCode.NOTICE_NOT_FOUND.getMessage());
		}
	}

	@Nested
	class DeleteNotice {
		@Test
		void 공지사항을_삭제한다() {
			// given
			Notice notice = Notice.builder()
				.title("title")
				.content("content")
				.startTime(LocalDateTime.of(2024, 4, 19, 13, 45))
				.endTime(LocalDateTime.of(2024, 4, 20, 13, 45))
				.writer("writer")
				.build();

			File file1 = new File();
			File file2 = new File();
			List<File> files = Arrays.asList(file1, file2);

			given(noticeRepository.findById(anyLong())).willReturn(java.util.Optional.of(notice));
			given(fileRepository.findAllByNoticeId(anyLong())).willReturn(files);
			willDoNothing().given(noticeRepository).delete(notice);
			willDoNothing().given(fileRepository).deleteAll(files);

			// when
			noticeService.deleteNotice(1L);

			// then
			then(noticeRepository).should().findById(1L);
			then(fileRepository).should().findAllByNoticeId(1L);
			then(noticeRepository).should().delete(notice);
			then(fileRepository).should().deleteAll(files);
		}

		@Test
		void 공지사항이_없는_경우_예외를_발생시킨다() {
			// given
			given(noticeRepository.findById(anyLong())).willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> noticeService.deleteNotice(1L))
				.isInstanceOf(EntityNotFoundException.class)
				.hasMessage(ErrorCode.NOTICE_NOT_FOUND.getMessage());
		}
	}

	@Nested
	class GetNotice {
		@Test
		void 공지사항을_조회한다() {
			// given
			Notice notice = Notice.builder()
				.title("title")
				.content("content")
				.startTime(LocalDateTime.of(2024, 4, 19, 13, 45))
				.endTime(LocalDateTime.of(2024, 4, 20, 13, 45))
				.writer("writer")
				.build();
			ReflectionTestUtils.setField(notice, "id", 1L);

			File file1 = new File(1L, "file1", ".png");
			File file2 = new File(1L, "file2", ".png");
			List<File> files = Arrays.asList(file1, file2);

			given(noticeRepository.findById(anyLong())).willReturn(java.util.Optional.of(notice));
			given(fileRepository.findAllByNoticeId(anyLong())).willReturn(files);

			// when
			NoticeResponse result = noticeService.getNotice(1L);

			// then
			assertThat(result.notice()).usingRecursiveComparison().isEqualTo(NoticeInfo.from(notice));
			assertThat(result.files()).hasSize(2);
			assertThat(result.files().get(0).originalName()).isEqualTo(file1.getOriginalName());
			assertThat(result.files().get(0).fileUrl()).isEqualTo(file1.getFileName());
			assertThat(result.files().get(1).originalName()).isEqualTo(file2.getOriginalName());
			assertThat(result.files().get(1).fileUrl()).isEqualTo(file2.getFileName());
		}

		@Test
		void 공지사항이_없는_경우_예외를_발생시킨다() {
			// given
			given(noticeRepository.findById(anyLong())).willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> noticeService.getNotice(1L))
				.isInstanceOf(EntityNotFoundException.class)
				.hasMessage(ErrorCode.NOTICE_NOT_FOUND.getMessage());
		}
	}

	@Test
	void 공지사항을_모두_조회한다() {
		// given
		Notice notice1 = Notice.builder()
			.title("title1")
			.content("content1")
			.startTime(LocalDateTime.of(2024, 4, 19, 13, 45))
			.endTime(LocalDateTime.of(2024, 4, 20, 13, 45))
			.writer("writer1")
			.build();
		Notice notice2 = Notice.builder()
			.title("title2")
			.content("content2")
			.startTime(LocalDateTime.of(2024, 4, 19, 13, 45))
			.endTime(LocalDateTime.of(2024, 4, 20, 13, 45))
			.writer("writer2")
			.build();
		List<Notice> notices = Arrays.asList(notice1, notice2);

		given(noticeRepository.findAll()).willReturn(notices);

		// when
		NoticesResponse result = noticeService.getNotices();

		// then
		assertThat(result.notices()).hasSize(2);
		assertThat(result.notices().get(0)).usingRecursiveComparison().isEqualTo(NoticeInfo.from(notice1));
		assertThat(result.notices().get(1)).usingRecursiveComparison().isEqualTo(NoticeInfo.from(notice2));
	}
}
