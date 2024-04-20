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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noticemanagement.notice.api.dto.request.NoticeCreateRequest;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class NoticeControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

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

		@AfterEach
		void cleanUp() {
			new File("1.txt").delete();
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
}
