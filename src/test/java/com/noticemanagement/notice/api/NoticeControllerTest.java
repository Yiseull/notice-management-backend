package com.noticemanagement.notice.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.File;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noticemanagement.notice.api.dto.request.NoticeCreateRequest;
import com.noticemanagement.notice.api.dto.request.NoticeModifyRequest;
import com.noticemanagement.notice.domain.Notice;
import com.noticemanagement.notice.fixtures.builder.NoticeBuilder;
import com.noticemanagement.notice.fixtures.setup.NoticeSetUp;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class NoticeControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	NoticeSetUp noticeSetUp;

	@AfterEach
	void cleanUp() {
		new File("src/test/resources/static/1.txt").delete();
		new File("src/test/resources/static/2.txt").delete();
	}

	@Nested
	class CreateNotice {

		MockMultipartFile json;

		@BeforeEach
		void setUp() throws JsonProcessingException {
			NoticeCreateRequest noticeCreateRequest = new NoticeCreateRequest(
				"제목",
				"내용",
				LocalDateTime.of(2024, 4, 19, 13, 45),
				LocalDateTime.of(2024, 4, 20, 13, 45),
				"작성자"
			);
			json = new MockMultipartFile("request", "", "application/json",
				objectMapper.writeValueAsString(noticeCreateRequest).getBytes());
		}

		@Test
		void 첨부파일이_없는_공지사항을_등록한다() throws Exception {
			// when
			ResultActions resultActions = mockMvc.perform(multipart("/api/notices")
				.file(json));

			// then
			resultActions
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("noticeId").value(1L));
		}

		@Test
		void 첨부파일이_있는_공지사항을_등록한다() throws Exception {
			// given
			MockMultipartFile file = new MockMultipartFile("files", "test.txt", "text/plain", "test".getBytes());

			// when
			ResultActions resultActions = mockMvc.perform(multipart("/api/notices")
				.file(json)
				.file(file));

			// then
			resultActions
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("noticeId").value(1L));
		}
	}

	@Nested
	class ModifyNotice {

		MockMultipartFile json;

		@BeforeEach
		void setUp() throws JsonProcessingException {
			noticeSetUp.save(NoticeBuilder.build());

			NoticeModifyRequest request = new NoticeModifyRequest("제목2", "내용2");
			json = new MockMultipartFile("request", "", "application/json",
				objectMapper.writeValueAsString(request).getBytes());
		}

		@Test
		void 첨부파일이_없는_공지사항을_수정한다() throws Exception {
			// when
			ResultActions resultActions = mockMvc.perform(multipart(HttpMethod.PUT, "/api/notices/1")
				.file(json));

			// then
			resultActions
				.andExpect(status().isOk());
		}

		@Test
		void 첨부파일이_있는_공지사항을_수정한다() throws Exception {
			// given
			MockMultipartFile file = new MockMultipartFile("files", "test.txt", "text/plain", "test".getBytes());

			// when
			ResultActions resultActions = mockMvc.perform(multipart(HttpMethod.PUT, "/api/notices/1")
				.file(json)
				.file(file));

			// then
			resultActions
				.andExpect(status().isOk());
		}
	}

	@Test
	void 공지사항을_삭제한다() throws Exception {
		// given
		noticeSetUp.save(NoticeBuilder.build());

		// when
		ResultActions resultActions = mockMvc.perform(delete("/api/notices/1"));

		// then
		resultActions
			.andExpect(status().isOk());
	}

	@Test
	void 공지사항을_조회한다() throws Exception {
		// given
		Notice notice = noticeSetUp.save(NoticeBuilder.build());

		// when
		ResultActions resultActions = mockMvc.perform(get("/api/notices/1"));

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("notice.noticeId").value(1L))
			.andExpect(jsonPath("notice.title").value(notice.getTitle().getTitle()))
			.andExpect(jsonPath("notice.content").value(notice.getContent()))
			.andExpect(jsonPath("notice.writer").value(notice.getWriter()))
			.andExpect(jsonPath("notice.views").value(1));
	}

	@Test
	void 공지사항을_모두_조회한다() throws Exception {
		// given
		Notice notice1 = noticeSetUp.save(NoticeBuilder.build("제목1", "내용1", "작성자1"));
		Notice notice2 = noticeSetUp.save(NoticeBuilder.build("제목2", "내용2", "작성자2"));

		// when
		ResultActions resultActions = mockMvc.perform(get("/api/notices"));

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("notices[0].noticeId").value(1L))
			.andExpect(jsonPath("notices[0].title").value(notice1.getTitle().getTitle()))
			.andExpect(jsonPath("notices[0].content").value(notice1.getContent()))
			.andExpect(jsonPath("notices[0].writer").value(notice1.getWriter()))
			.andExpect(jsonPath("notices[0].views").value(0))
			.andExpect(jsonPath("notices[1].noticeId").value(2L))
			.andExpect(jsonPath("notices[1].title").value(notice2.getTitle().getTitle()))
			.andExpect(jsonPath("notices[1].content").value(notice2.getContent()))
			.andExpect(jsonPath("notices[1].writer").value(notice2.getWriter()))
			.andExpect(jsonPath("notices[1].views").value(0));
	}
}
