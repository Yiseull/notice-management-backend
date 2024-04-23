package com.noticemanagement.notice.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record NoticeModifyRequest(
	@Schema(description = "제목", example = "공지사항 제목")
	String title,

	@Schema(description = "내용", example = "공지사항 내용")
	String content
) {
}
